package com.example.socialbundle

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.MainActivity.Companion.auth
import com.example.socialbundle.databinding.ActivityCreateBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CreateActivity : AppCompatActivity() {

    private lateinit var selectedGender: String
    lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.dob.setOnClickListener {
            showDatePickerDialog()
        }

        binding.passwordShowHide2.setOnClickListener {
            val isPasswordVisible =
                binding.password.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())   // Checks whether the password is Hidden or not?

            // If Password Visible
            if (isPasswordVisible) {

                binding.password.transformationMethod =
                    PasswordTransformationMethod.getInstance()     // Decrypt the password
                binding.passwordShowHide2.setImageResource(R.drawable.ic_show_pwd)             // Changes icon  of ic_hide_pwd to ic_show_pwd

            }
            // If Password not Visible
            else {

                binding.password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()      // Encrypt the password
                binding.passwordShowHide2.setImageResource(R.drawable.ic_hide_pwd)             // Changes icon  of ic_show_pwd to ic_hide_pwd

            }
            binding.password.setSelection(binding.password.text.length)     // Move the cursor to the end of password
        }

        binding.signUp.setOnClickListener {

            // Retrieving email and password from the input fields
            val email: String? = binding.emails.text.toString()
            val pass: String? = binding.password.text.toString()
            val confpass: String? = binding.confPassword.text.toString()
            val first: String? = binding.name1.text.toString()
            val contacts: String? = binding.contact.text.toString()
            val usernames: String? = binding.username.text.toString().trim()
            val dateofBirth: String? = binding.dob.text.toString()
            val selectedRadioButton = binding.genders.checkedRadioButtonId
            if (selectedRadioButton == -1) {
                Toast.makeText(this,"Gender not selected",Toast.LENGTH_SHORT).show()
            }else{
                val radioButton = findViewById<RadioButton>(selectedRadioButton)
                selectedGender = radioButton.text.toString()
            }

            if (first.isNullOrEmpty()) {
                binding.name1.error = "Full Name is required"
                binding.name1.requestFocus()
            }

            if (email.isNullOrEmpty()) {
                binding.emails.error = "Email ID is required"
                binding.emails.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emails.text).matches()) {
                binding.emails.error = "Enter Valid Email"
                binding.emails.requestFocus()
            }

            if (contacts.isNullOrEmpty()) {
                binding.contact.error = "Contact Number is required"
                binding.contact.requestFocus()
            }

            if (usernames.isNullOrEmpty()) {
                binding.username.error = "Username is required"
                binding.username.requestFocus()
            } else if (usernames.contains('\t')) {
                binding.username.error = "Username can't contain Spaces"
                binding.username.requestFocus()
            }

            if (dateofBirth.isNullOrEmpty()) {
                binding.dob.error = "Date of Birth is required"
                binding.dob.requestFocus()
            }

            if (pass.isNullOrEmpty()) {
                binding.password.error = "Password is required"
                binding.password.requestFocus()
            } else if (!pass.equals(confpass)) {
                binding.confPassword.error = "Password did not matched"
                binding.confPassword.requestFocus()
                binding.password.clearComposingText()                        // Written Password is cleared
                binding.confPassword.clearComposingText()                    // Written Confirm Password is cleared
            } else if (binding.password.length() <= 6) {
                binding.password.error = "Password is Weak"
                binding.password.requestFocus()
            }

            if (confpass.isNullOrEmpty()) {
                binding.confPassword.error = "Confirm your password"
                binding.confPassword.requestFocus()
            }


            // Creating a new user with email and password using Firebase authentication

            if (!email.isNullOrEmpty()) {
                if (!pass.isNullOrEmpty() && pass.equals(confpass)) {
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            val userId = firebaseUser?.uid
                            val reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)
                            val hashMap = HashMap<String, Any>()
                            hashMap["id"] = userId
                            hashMap["username"] = usernames!!.lowercase()
                            hashMap["fullName"] = first ?: ""
                            hashMap["emailId"] = email
                            hashMap["phoneNumber"] = contacts.toString()
                            hashMap["gender"] = selectedGender
                            hashMap["bio"] =
                                "Hello" // Replace with your desired default value for "bio"
                            hashMap["dateofbirth"] = dateofBirth ?: ""
                            hashMap["imageurl"] =
                                "https://firebasestorage.googleapis.com/v0/b/sociox-0007.appspot.com/o/user_747376.png?alt=media&token=b2fa58f4-5443-44b5-8977-26f6067c6a04"
                            reference.setValue(hashMap).addOnCompleteListener {
                                // If user creation is successful, navigate to the SocioActivity
                                startActivity(Intent(this, SocioActivity::class.java))
                                finish()
                                Log.d("Random", "$userId")
                            }
                        }
                    }.addOnFailureListener {
                        // If an error occurs during user creation, display an error message
                        binding.progressBar1.visibility = View.INVISIBLE
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { _, selectedYear, selectedMonth, selectedDay ->

                // Format the selected date
                val selectedDate = formatDate(selectedDay, selectedMonth, selectedYear)
                Log.d("Rupesh", "$selectedDay And $selectedDate")
                // Update the EditText with the selected date
                binding.dob.setText(selectedDate)

            },
            year,
            month,
            day
        )

        // Set the maximum date to the current date to prevent future dates
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        // Show the date picker dialog
        datePickerDialog.show()
    }


    private fun formatDate(day: Int, month: Int, year: Int): String {
        // Create a SimpleDateFormat to format the date
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Create a Calendar object and set it to the selected date
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        // Format the date and return the formatted string
        return simpleDateFormat.format(calendar.time)
    }
}