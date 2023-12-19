package com.example.socialbundle.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.databinding.ActivityForgotBinding
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ForgotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.sendOtp.setOnClickListener {

            binding.progressBar2.visibility = View.VISIBLE

            val emails: String? = binding.emailEdittext.text.toString()

            // Assuming "user" is the root node in your Realtime Database
            val databaseReference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(USER_NODE)

            if (emails.isNullOrEmpty()) {
                binding.emailEdittext.error = "Enter Email"
                binding.emailEdittext.requestFocus()
                binding.progressBar2.visibility = View.INVISIBLE
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text).matches()) {
                binding.emailEdittext.error = "Enter Email in correct Form"
                binding.emailEdittext.requestFocus()
                binding.progressBar2.visibility = View.INVISIBLE
            } else {
                binding.sendOtp.isEnabled = false
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var emailFound = false

                        for (childSnapshot in dataSnapshot.children) {
                            val email = childSnapshot.child("emailId").getValue(String::class.java)

                            if (email == emails) {
                                // Email found
                                emailFound = true
                                break
                            }
                        }

                        if (emailFound) {
                            FirebaseAuth.getInstance()
                                .sendPasswordResetEmail(emails.toString()).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Password reset email sent successfully
                                        Toast.makeText(
                                            this@ForgotActivity,
                                            "Password reset email sent",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(
                                            Intent(
                                                this@ForgotActivity,
                                                LoginActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        binding.sendOtp.isEnabled = true
                                        // Failed to send password reset email
                                        binding.progressBar2.visibility = View.INVISIBLE
                                        Toast.makeText(
                                            this@ForgotActivity,
                                            "Something went wrong",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.d(
                                            "PasswordReset",
                                            "Failed to send password reset email"
                                        )
                                    }
                                }
                        } else {
                            binding.sendOtp.isEnabled = true
                            binding.progressBar2.visibility = View.INVISIBLE
                            binding.emailEdittext.error = "$emails : not Registered"
                            binding.emailEdittext.requestFocus()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        binding.sendOtp.isEnabled = true
                        binding.progressBar2.visibility = View.INVISIBLE
                        println("Error: ${databaseError.message}")
                    }
                })
            }
        }

    }
}