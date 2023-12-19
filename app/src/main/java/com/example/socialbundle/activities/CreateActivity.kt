package com.example.socialbundle.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.R
import com.example.socialbundle.databinding.ActivityCreateBinding
import com.example.socialbundle.utils.PROFILE_IMAGES_NODE
import com.example.socialbundle.utils.USER_IMAGES_NODE
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var storage: FirebaseStorage
    private var uri: Uri?=null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()

        val galleryImage= registerForActivityResult(ActivityResultContracts.GetContent()){ imageuri->
            binding.profileImage.setImageURI(imageuri)
            uri=imageuri
        }

        binding.addYourProfilePic.setOnClickListener {
            galleryImage.launch("image/*")
        }

        val firstWord = "Already have an account?"
        val secondWord = "Login"

        val spannableStringBuilder =
            SpannableStringBuilder(firstWord).append(" ").append(secondWord)

        // Style and color the first word
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.NORMAL),
            0,
            firstWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            firstWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Style and color the second word
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            firstWord.length + 1,
            spannableStringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(Color.BLUE),
            firstWord.length + 1,
            spannableStringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.login1.text = spannableStringBuilder

        binding.login1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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

        // Setting up the click listener for the sign-up button
        binding.signUp.setOnClickListener {
            // Disabling the sign-up button to prevent multiple clicks
            binding.signUp.isEnabled = false
            binding.progressBar1.visibility = View.VISIBLE

            // Retrieving user input data
            val email: String = binding.emails.text.toString()
            val pass: String = binding.password.text.toString()
            val confpass: String = binding.confPassword.text.toString()
            val name: String = binding.name1.text.toString()
            val usernames: String = binding.username.text.toString().trim()

            // Validation for Full Name
            if (name.isEmpty()) {
                binding.name1.error = "Full Name is required"
                binding.name1.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            }

            // Validation for Email
            if (email.isEmpty()) {
                binding.emails.error = "Email ID is required"
                binding.emails.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emails.text).matches()) {
                binding.emails.error = "Enter Valid Email"
                binding.emails.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            }

            // Validation for Username
            if (usernames.isEmpty()) {
                binding.username.error = "Username is required"
                binding.username.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            } else if (usernames.contains('\t')) {
                binding.username.error = "Username can't contain Spaces"
                binding.username.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            }

            // Validation for Password
            if (pass.isEmpty()) {
                binding.password.error = "Password is required"
                binding.password.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            } else if (binding.password.length() <= 6) {
                binding.password.error = "Password is Weak"
                binding.password.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            }

            // Validation for Confirm Password
            if (confpass.isEmpty()) {
                binding.confPassword.error = "Confirm your password"
                binding.confPassword.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            } else if (!confpass.equals(pass)) {
                binding.confPassword.error = "Password did not match"
                binding.confPassword.requestFocus()
                binding.password.clearComposingText()
                binding.confPassword.clearComposingText()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            }

            // Validation for accepting terms and conditions
            if (!binding.termsCheck.isChecked) {
                binding.terms.error = "Accept Terms and conditions "
                binding.terms.requestFocus()
                binding.signUp.isEnabled = true
                binding.progressBar1.visibility = View.INVISIBLE
            }

            // Creating a new user with email and password using Firebase authentication
            else if (email.isNotEmpty() && pass.isNotEmpty() && usernames.isNotEmpty() && confpass.isNotEmpty() && name.isNotEmpty() && binding.password.length() >= 6){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { registrationTask ->
                        if (registrationTask.isSuccessful) {
                            val firebaseUser = FirebaseAuth.getInstance().currentUser
                            val userId = firebaseUser?.uid

                            // Function to upload image to Firebase Storage and update user data
                            fun uploadImageAndSetUserData(uri: Uri?) {
                                val reference = FirebaseDatabase.getInstance()
                                    .getReference(USER_NODE)
                                    .child(userId!!)

                                // Default image URL if no image is selected
                                val defaultImageUrl =
                                    "https://firebasestorage.googleapis.com/v0/b/sociox-0007.appspot.com/o/profile_img.png?alt=media&token=014a0f9a-6c95-4839-bc6f-c26218abfdc6"

                                if (uri != null) {
                                    storage.getReference(USER_IMAGES_NODE).child(PROFILE_IMAGES_NODE)
                                        .child(userId).child(System.currentTimeMillis().toString())
                                        .putFile(uri)
                                        .addOnSuccessListener { task ->
                                            task.metadata?.reference?.downloadUrl
                                                ?.addOnSuccessListener { selectedImage ->
                                                    val hashMap = hashMapOf(
                                                        "id" to userId,
                                                        "username" to usernames.lowercase(),
                                                        "name" to name,
                                                        "mobilenumber" to "",
                                                        "emailId" to email,
                                                        "bio" to "default bio",
                                                        "dateofbirth" to "",
                                                        "gender" to "",
                                                        "imageurl" to selectedImage.toString()
                                                    )

                                                    reference.setValue(hashMap)
                                                        .addOnCompleteListener {
                                                            val intent = Intent(
                                                                this,
                                                                SocioActivity::class.java
                                                            )
                                                            intent.flags =
                                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                            startActivity(intent)
                                                            finish()
                                                            Log.d("Random", "$userId")
                                                        }
                                                }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                this,
                                                "Image Upload not successfull!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } else {
                                    // No image selected, use default image URL
                                    val hashMap = hashMapOf(
                                        "id" to userId,
                                        "username" to usernames.lowercase(),
                                        "name" to name,
                                        "emailId" to email,
                                        "bio" to "default bio",
                                        "imageurl" to defaultImageUrl
                                    )

                                    reference.setValue(hashMap)
                                        .addOnCompleteListener {
                                            val intent = Intent(this, SocioActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            finish()
                                            Log.d("Random", "$userId")
                                        }
                                }
                            }

                            // Call the function with the provided URI
                            uploadImageAndSetUserData(uri)
                        }
                    }
                    .addOnFailureListener { registrationFailure ->
                        // If an error occurs during user creation, display an error message
                        binding.signUp.isEnabled = true
                        binding.progressBar1.visibility = View.INVISIBLE
                        Toast.makeText(
                            this,
                            registrationFailure.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }



    }
}