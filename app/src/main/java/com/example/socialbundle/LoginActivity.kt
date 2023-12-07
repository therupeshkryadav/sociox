package com.example.socialbundle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.example.socialbundle.MainActivity.Companion.auth
import com.example.socialbundle.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, SocioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Handling the "Create" button click to navigate to the CreateActivity
        binding.create.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
            finish()
        }

        binding.forgot.setOnClickListener{
            startActivity(Intent(this, ForgotActivity::class.java))
        }

        binding.passwordShowHide1.setOnClickListener {
            val isPasswordVisible = binding.editTextTextPassword.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())  // Checks whether the password is Hidden or not?

            // If Password Visible
            if(isPasswordVisible) {

                binding.editTextTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()     // Decrypt the password
                binding.passwordShowHide1.setImageResource(R.drawable.ic_show_pwd)             // Changes icon  of ic_hide_pwd to ic_show_pwd

            }
            // If Password not Visible
            else{

            binding.editTextTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()      // Encrypt the password
            binding.passwordShowHide1.setImageResource(R.drawable.ic_hide_pwd)             // Changes icon  of ic_show_pwd to ic_hide_pwd

        }
            binding.editTextTextPassword.setSelection(binding.editTextTextPassword.text.length)     // Move the cursor to the end of password
        }

        // Handling the "Login" button click
        binding.login.setOnClickListener {
            binding.progressBars.visibility = View.VISIBLE
            // Retrieving email and password from the input fields
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()

            // Checking if email and password are not empty
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // Signing in with email and password using Firebase authentication
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If login is successful, get the current user's UID
                        val userId = auth.currentUser?.uid.toString()

                        // Getting a reference to the "Users" node in the Firebase Realtime Database
                        reference = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                        // Adding ValueEventListener to fetch additional user information
                        reference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                // Navigating to SocioActivity upon successful login
                                startActivity(Intent(this@LoginActivity, SocioActivity::class.java))
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle database error if needed
                                binding.progressBars.visibility = View.INVISIBLE
                                Toast.makeText(this@LoginActivity, "Database Error: ${error.message}", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }.addOnFailureListener { exception ->
                    // Displaying a Toast message in case of login failure
                    binding.progressBars.visibility = View.INVISIBLE
                    Toast.makeText(this,"Email address or Password is wrong!!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}