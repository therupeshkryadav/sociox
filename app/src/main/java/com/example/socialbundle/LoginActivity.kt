package com.example.socialbundle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.example.socialbundle.databinding.ActivityLoginBinding
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val intent = Intent(this, SocioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Handling the "Login" button click
        binding.login.setOnClickListener {

            // Disable the login button to prevent multiple clicks
            binding.login.isEnabled = false

            // Retrieving email and password from the input fields
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()

            // Checking if email and password are not empty
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.progressBars.visibility = View.VISIBLE

                // Signing in with email and password using Firebase authentication
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // If login is successful, get the current user's UID
                            val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

                            // Getting a reference to the "Users" node in the Firebase Realtime Database
                            reference = FirebaseDatabase.getInstance().reference.child(USER_NODE).child(userId)

                            // Adding ValueEventListener to fetch additional user information
                            reference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    // Navigating to SocioActivity upon successful login
                                    startActivity(Intent(this@LoginActivity, SocioActivity::class.java))
                                    finish()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle database error if needed
                                    handleLoginError()
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Database Error: ${error.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                        } else {
                            // Displaying a Toast message in case of login failure
                            handleLoginError()
                            Toast.makeText(
                                this@LoginActivity,
                                "Authentication Failed: ${task.exception?.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                // Enable the login button and show a Toast message for incomplete input
                handleLoginError()
                Toast.makeText(this@LoginActivity, "Fill all the required inputs", Toast.LENGTH_SHORT).show()
            }
        }

        // Handling the "Create" button click to navigate to the CreateActivity
        binding.create.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))

        }

        binding.forgot.setOnClickListener {
            startActivity(Intent(this, ForgotActivity::class.java))
        }

        binding.passwordShowHide1.setOnClickListener {
            // Checks whether the password is currently hidden or not
            val isPasswordVisible =
                binding.editTextTextPassword.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())

            // If Password is currently visible, hide it
            if (isPasswordVisible) {
                binding.editTextTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordShowHide1.setImageResource(R.drawable.ic_show_pwd)
            }
            // If Password is currently hidden, make it visible
            else {
                binding.editTextTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.passwordShowHide1.setImageResource(R.drawable.ic_hide_pwd)
            }

            // Move the cursor to the end of the password
            binding.editTextTextPassword.setSelection(binding.editTextTextPassword.text.length)
        }
    }

    private fun handleLoginError() {
        // Enable the login button and hide the progress bar
        binding.login.isEnabled = true
        binding.progressBars.visibility = View.INVISIBLE

    }
}
