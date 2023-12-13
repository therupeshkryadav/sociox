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
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.MainActivity.Companion.auth
import com.example.socialbundle.databinding.ActivityCreateBinding
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CreateActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateBinding
    lateinit var storage: FirebaseStorage
    var uri:Uri?=null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()

        val galleryImage= registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.profileImage.setImageURI(it)
            uri= it!!
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

        binding.signUp.setOnClickListener {

            binding.signUp.isEnabled = false

            // Retrieving email and password from the input fields
            val email: String = binding.emails.text.toString()
            val pass: String = binding.password.text.toString()
            val confpass: String = binding.confPassword.text.toString()
            val name: String = binding.name1.text.toString()
            val usernames: String = binding.username.text.toString().trim()


            if (name.isEmpty()) {
                binding.name1.error = "Full Name is required"
                binding.name1.requestFocus()
                binding.signUp.isEnabled = true
            }

            if (email.isEmpty()) {
                binding.emails.error = "Email ID is required"
                binding.emails.requestFocus()
                binding.signUp.isEnabled = true
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emails.text).matches()) {
                binding.emails.error = "Enter Valid Email"
                binding.emails.requestFocus()
                binding.signUp.isEnabled = true
            }

            if (usernames.isEmpty()) {
                binding.username.error = "Username is required"
                binding.username.requestFocus()
                binding.signUp.isEnabled = true
            } else if (usernames.contains('\t')) {
                binding.username.error = "Username can't contain Spaces"
                binding.username.requestFocus()
                binding.signUp.isEnabled = true
            }

            if (pass.isEmpty()) {
                binding.password.error = "Password is required"
                binding.password.requestFocus()
                binding.signUp.isEnabled = true
            } else if (!pass.equals(confpass)) {
                binding.confPassword.error = "Password did not matched"
                binding.confPassword.requestFocus()
                binding.password.clearComposingText()                        // Written Password is cleared
                binding.confPassword.clearComposingText()
                binding.signUp.isEnabled = true// Written Confirm Password is cleared
            } else if (binding.password.length() <= 6) {
                binding.password.error = "Password is Weak"
                binding.password.requestFocus()
                binding.signUp.isEnabled = true
            }

            if (confpass.isEmpty()) {
                binding.confPassword.error = "Confirm your password"
                binding.confPassword.requestFocus()
                binding.signUp.isEnabled = true
            }


            // Creating a new user with email and password using Firebase authentication

            if (email.isNotEmpty() && pass.isNotEmpty() && pass == confpass) {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { registrationTask ->
                        if (registrationTask.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            val userId = firebaseUser?.uid

                            // Function to upload image to Firebase Storage and update user data
                            fun uploadImageAndSetUserData(uri: Uri?) {
                                val reference = FirebaseDatabase.getInstance().getReference(USER_NODE)
                                    .child(userId!!)

                                // Default image URL if no image is selected
                                val defaultImageUrl =
                                    "https://firebasestorage.googleapis.com/v0/b/sociox-0007.appspot.com/o/profile_img.png?alt=media&token=014a0f9a-6c95-4839-bc6f-c26218abfdc6"

                                // If no image is selected, use the default image URL
                                val imageUrl = uri?.let { selectedUri ->
                                    storage.getReference("images").child(System.currentTimeMillis().toString())
                                        .putFile(selectedUri)
                                        .addOnSuccessListener { task ->
                                            task.metadata?.reference?.downloadUrl
                                                ?.addOnSuccessListener { selectedImage ->
                                                    // Set user data with the selected image URL
                                                    val hashMap = hashMapOf(
                                                        "id" to userId,
                                                        "username" to usernames.lowercase(),
                                                        "name" to (name ?: ""),
                                                        "emailId" to email,
                                                        "bio" to "default bio",
                                                        "imageurl" to selectedImage.toString()
                                                    )

                                                    reference.setValue(hashMap)
                                                        .addOnCompleteListener {
                                                            // If user creation is successful, navigate to SocioActivity
                                                            startActivity(Intent(this, SocioActivity::class.java))
                                                            finish()
                                                            Log.d("Random", "$userId")
                                                        }
                                                }
                                        }
                                } ?: run {
                                    // No image selected, use default image URL
                                    val hashMap = hashMapOf(
                                        "id" to userId,
                                        "username" to usernames.lowercase(),
                                        "name" to (name ?: ""),
                                        "emailId" to email,
                                        "bio" to "default bio",
                                        "imageurl" to defaultImageUrl
                                    )

                                    reference.setValue(hashMap)
                                        .addOnCompleteListener {
                                            // If user creation is successful, navigate to SocioActivity
                                            startActivity(Intent(this, SocioActivity::class.java))
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
                        Toast.makeText(this, registrationFailure.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }


        }


    }
}

