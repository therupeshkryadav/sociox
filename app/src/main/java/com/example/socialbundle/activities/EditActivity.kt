package com.example.socialbundle.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.databinding.ActivityEditBinding
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedGender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        }

    override fun onStart() {
        super.onStart()

        storage = FirebaseStorage.getInstance()

        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.editProfileImage.setImageURI(uri)
        }

        binding.changeDateofbirth.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editProfile.setOnClickListener {
            galleryImage.launch("image/*")
        }

        val current = FirebaseAuth.getInstance().currentUser

        databaseReference = FirebaseDatabase.getInstance().getReference(USER_NODE)
            .child(current?.uid ?: "defaultNode")

        // Read from the database
        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val userName = snapshot.child("username").value
                val fullName = snapshot.child("name").value
                val emailAdd = snapshot.child("emailId").value
                val bIo = snapshot.child("bio").value
                val dateOfBirth = snapshot.child("dateofbirth").value
                val gEnder = snapshot.child("gender").value
                selectedGender=gEnder.toString()
                val iMAGEurl = snapshot.child("imageurl").value
                val moBilNumber = snapshot.child("mobilenumber").value

                when (gEnder) {
                    "Male" -> {
                        val maleRadioButton = findViewById<RadioButton>(binding.maleRadioButton.id)
                        maleRadioButton.isChecked = true
                    }

                    "Female" -> {
                        val femaleRadioButton =
                            findViewById<RadioButton>(binding.femaleRadioButton.id)
                        femaleRadioButton.isChecked = true
                    }

                    "Others" -> {
                        val othersRadioButton =
                            findViewById<RadioButton>(binding.othersRadioButton.id)
                        othersRadioButton.isChecked = true
                    }
                }

                // Perform null checks before assigning values to UI elements
                binding.changeName.setText(fullName.toString())
                binding.changeDateofbirth.setText(dateOfBirth.toString())
                binding.changeMobileNumber.setText(moBilNumber.toString())
                binding.changeUsername.setText(userName.toString())
                binding.changeEmailID.setText(emailAdd.toString())
                binding.changeBio.setText(bIo.toString())
                Picasso.get().load(iMAGEurl.toString()).into(binding.editProfileImage)
            } else {
                Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

        binding.updateProfile.setOnClickListener {
            val newFullName = binding.changeName.text.toString()
            val newUserName = binding.changeUsername.text.toString()
            val newEmail = binding.changeEmailID.text.toString()
            val newBio = binding.changeBio.text.toString()
            val newMobile = binding.changeMobileNumber.text.toString()
            val newDateOfBirth = binding.changeDateofbirth.text.toString()
            val selectedRadioButton = binding.genders.checkedRadioButtonId
            if (selectedRadioButton != -1) {
                val radioButton = findViewById<RadioButton>(selectedRadioButton)
                selectedGender = radioButton.text.toString()
            }
            updateUserData(
                newFullName,
                newUserName,
                newEmail,
                newBio,
                newMobile,
                newDateOfBirth,
                selectedGender,
                this
            )
            supportFragmentManager.popBackStack()
            // Set a result flag to indicate that the profile has been updated
            val resultIntent = Intent()
            resultIntent.putExtra("PROFILE_UPDATED", true)
            setResult(RESULT_OK, resultIntent)

            // Finish the EditActivity
            finish()
    }
    }

    private fun updateUserData(
        newFullName: String,
        newUserName: String,
        newEmail: String,
        newBio: String,
        newMobile: String,
        newDateOfBirth: String,
        selectedGender: String,
        callingActivity: Activity
    ) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {
            val userReference =
                FirebaseDatabase.getInstance().getReference(USER_NODE).child(currentUserId)

            // Update the user data including the new image URL
            userReference.child("name").setValue(newFullName)
            userReference.child("username").setValue(newUserName)
            userReference.child("emailId").setValue(newEmail)
            userReference.child("bio").setValue(newBio)
            userReference.child("mobilenumber").setValue(newMobile)
            userReference.child("dateofbirth").setValue(newDateOfBirth)
            userReference.child("gender").setValue(selectedGender)

            // Set a result flag to indicate that the profile has been updated
            val resultIntent = Intent()
            resultIntent.putExtra("PROFILE_UPDATED", true)
            callingActivity.setResult(Activity.RESULT_OK, resultIntent)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->

                // Format the selected date
                val selectedDate = formatDate(selectedDay, selectedMonth, selectedYear)
                Log.d("Rupesh", "$selectedDay And $selectedDate")
                // Update the EditText with the selected date
                binding.changeDateofbirth.setText(selectedDate)

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