package com.example.socialbundle

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.MainActivity.Companion.auth
import com.example.socialbundle.databinding.ActivityForgotBinding
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
                FirebaseDatabase.getInstance().getReference("Users")

            val targetEmail = emails // The email you want to search for

            if (emails.isNullOrEmpty()) {
                binding.emailEdittext.error = "Enter Email"
                binding.emailEdittext.requestFocus()
                binding.progressBar2.visibility = View.INVISIBLE
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text).matches()) {
                binding.emailEdittext.error = "Enter Email in correct Form"
                binding.emailEdittext.requestFocus()
                binding.progressBar2.visibility = View.INVISIBLE
            } else {
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var emailFound = false

                        for (childSnapshot in dataSnapshot.children) {
                            val email = childSnapshot.child("emailId").getValue(String::class.java)

                            if (email == targetEmail) {
                                // Email found
                                emailFound = true
                                break
                            }
                        }

                        if (emailFound) {
                            auth.sendPasswordResetEmail(emails.toString()).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Password reset email sent successfully
                                        Toast.makeText(this@ForgotActivity, "Password reset email sent", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@ForgotActivity, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        // Failed to send password reset email
                                        binding.progressBar2.visibility = View.INVISIBLE
                                        Toast.makeText(this@ForgotActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                                        Log.d("PasswordReset", "Failed to send password reset email")
                                    }
                                }
                        } else {
                            binding.progressBar2.visibility = View.INVISIBLE
                            binding.emailEdittext.error = "$targetEmail : not Registered"
                            binding.emailEdittext.requestFocus()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        binding.progressBar2.visibility = View.INVISIBLE
                        println("Error: ${databaseError.message}")
                    }
                })
//        binding.reset.setOnClickListener {
//            binding.progressBar2.visibility = View.VISIBLE
//
//            object : CountDownTimer(1000, 500) {
//
//                override fun onTick(millisUntilFinished: Long) {
//
//                }
//
//                override fun onFinish() {
//
//                    binding.linear2.visibility = View.INVISIBLE
//                    binding.linear4.visibility = View.VISIBLE
//                    binding.forgotAndOtpAndReset.text = changing[2]
//                    binding.progressBar2.visibility = View.GONE
//
//                }
//            }.start()
//        }

            }
        }
    }
}