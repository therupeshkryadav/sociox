package com.example.socialbundle

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.databinding.ActivityCreateBinding
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var fileUrl: String
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()

        // Set up activity result contract for getting images
        val galleryImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                binding.profileImage.setImageURI(uri)
                // ... (image upload logic)
            }

        // Click listener for selecting profile picture
        binding.addYourProfilePic.setOnClickListener {
            galleryImage.launch("image/*")
        }

        // Configure the "Already have an account? Login" text with different styles and colors
        configureLoginText()

        // Click listener for navigating to login activity
        binding.login1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Click listener for toggling password visibility
        binding.passwordShowHide2.setOnClickListener {
            togglePasswordVisibility()
        }

        // Click listener for signing up
        binding.signUp.setOnClickListener {
            // Disable signup button to prevent multiple clicks during the signup process
            binding.signUp.isEnabled = false

            // Retrieve user input
            val email: String = binding.emails.text.toString()
            val pass: String = binding.password.text.toString()
            val confpass: String = binding.confPassword.text.toString()
            val name: String = binding.name1.text.toString()
            val usernames: String = binding.username.text.toString().trim()

            // Validate user input
            if (name.isEmpty()) {
                showError("Full Name is required")
                binding.signUp.isEnabled = true
            }
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError("Enter a valid Email")
                binding.signUp.isEnabled = true
            }
            if (usernames.isEmpty() || usernames.contains('\t')) {
                showError("Invalid Username")
                binding.signUp.isEnabled = true
            }
            if (pass.isEmpty() || pass.length <= 6) {
                showError("Invalid Password")
                binding.signUp.isEnabled = true
            }
            if (confpass.isEmpty() || !pass.equals(confpass)) {
                showError("Passwords do not match")
                binding.signUp.isEnabled = true
            }

            // Default image URL if the user doesn't upload a profile picture
            if (fileUrl.isNullOrEmpty()) {
                fileUrl = "https://firebasestorage.googleapis.com/v0/b/sociox-0007.appspot.com/o/profile_img.png?alt=media&token=014a0f9a-6c95-4839-bc6f-c26218abfdc6"
            }

            // Create user with email and password using Firebase authentication
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { registrationTask ->
                    if (registrationTask.isSuccessful) {
                        // Registration successful, now add user data to the Firebase Realtime Database
                        val firebaseUser = auth.currentUser
                        val userId = firebaseUser?.uid
                        val database = FirebaseDatabase.getInstance().getReference(USER_NODE).child(userId!!)

                        // Create a HashMap to store user data
                        val userData = HashMap<String, Any>()
                        userData["name"] = name
                        userData["email"] = email
                        userData["username"] = usernames
                        userData["imageurl"] = fileUrl
                        userData["mobilenumber"] = ""
                        userData["dateofbirth"] = ""
                        userData["gender"] = ""
                        userData["bio"] = ""

                        // Set the user data in the database
                        database.setValue(userData)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    // Signup and database update successful, navigate to the next activity
                                    startActivity(Intent(this,SocioActivity::class.java))
                                    finish()

                                } else {
                                    showError("Failed to update user data in the database")
                                }
                            }
                    }
                }
                .addOnFailureListener { registrationFailure ->
                    // If an error occurs during user creation, display an error message
                    binding.signUp.isEnabled = true
                    showError(registrationFailure.localizedMessage)
                }
        }
    }

    // Function to configure the "Already have an account? Login" text
    private fun configureLoginText() {
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
    }

    // Function to toggle password visibility
    private fun togglePasswordVisibility() {
        val isPasswordVisible =
            binding.password.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())

        // If Password is visible, hide it
        if (isPasswordVisible) {
            binding.password.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.passwordShowHide2.setImageResource(R.drawable.ic_show_pwd)
        } else {
            // If Password is hidden, show it
            binding.password.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.passwordShowHide2.setImageResource(R.drawable.ic_hide_pwd)
        }
        binding.password.setSelection(binding.password.text.length)
    }

    // Function to display error messages using Toast
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.signUp.isEnabled = true  // Enable the signup button after displaying the error
    }
}
