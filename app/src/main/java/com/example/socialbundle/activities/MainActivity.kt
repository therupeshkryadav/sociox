package com.example.socialbundle.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        // Use Handler to delay the start of the next activity
        Handler().postDelayed({
            // Create an Intent to start the next activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Finish the current activity to prevent going back to it
            finish()
        }, 1000)
    }
}