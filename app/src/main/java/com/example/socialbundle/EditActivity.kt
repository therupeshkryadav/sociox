package com.example.socialbundle

import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socialbundle.MainActivity.Companion.auth
import com.example.socialbundle.databinding.ActivityEditBinding
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var storage: FirebaseStorage
    var uri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()

        val galleryImage= registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.editProfileImage.setImageURI(it)
            uri= it!!
        }

        binding.editProfile.setOnClickListener {
            galleryImage.launch("image/*")
        }

        val current = auth.currentUser

        databaseReference =
            FirebaseDatabase.getInstance().getReference(USER_NODE)
                .child(current?.uid ?: "defaultNode")

     // Read from the database
        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val userName = snapshot.child("username").value
                val fullName = snapshot.child("name").value
                val emailAdd = snapshot.child("emailId").value
                val bIo = snapshot.child("bio").value
                val iMageUrl = snapshot.child("imageurl").value

                // Perform null checks before assigning values to UI elements
                binding.changeName.setText(fullName.toString())
                binding.changeUsername.setText(userName.toString())
                binding.changeEmailID.setText(emailAdd.toString())
                binding.changeBio.setText(bIo.toString())
                Picasso.get().load(iMageUrl.toString()).into(binding.editProfileImage)
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
            val contacts: String ?= binding.changeMobileNumber.text.toString()
            val dateofBirth: String ? = binding.changeDateofbirth.text.toString()
            val selectedRadioButton = binding.genders.checkedRadioButtonId
            if (selectedRadioButton == -1) {
                Toast.makeText(this,"Gender not selected",Toast.LENGTH_SHORT).show()
            }else{
                val radioButton = findViewById<RadioButton>(selectedRadioButton)
                var selectedGender = radioButton.text.toString()
            }

            // Update the user data in the database
            updateUserData(newFullName, newUserName, newEmail, newBio)

            // Inform the user that the profile has been updated
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()

            // Go back to the previous activity
            onBackPressed()
        }
    }

    private fun updateUserData(
        newFullName: String,
        newUserName: String,
        newEmail: String,
        newBio: String
    ) {
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            val userReference =
                FirebaseDatabase.getInstance().getReference(USER_NODE).child(currentUserId)

            // Update the user data
            userReference.child("name").setValue(newFullName)
            userReference.child("username").setValue(newUserName)
            userReference.child("emailId").setValue(newEmail)
            userReference.child("bio").setValue(newBio)
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
