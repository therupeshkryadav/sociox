package com.example.socialbundle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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