package com.example.socialbundle.UploadingWork

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.activities.SocioActivity
import com.example.socialbundle.databinding.ActivityUploadSocioClipsBinding
import com.example.socialbundle.utils.SOCIO_CLIPS_NODE
import com.example.socialbundle.utils.USER_SOCIO_CLIPS_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadSocioClipsActivity : AppCompatActivity() {

    private var uri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityUploadSocioClipsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadSocioClipsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()


        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userId = firebaseUser?.uid


        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, SocioActivity::class.java))
            finish()
        }
        val galleryImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) { imageuri ->
                uri = imageuri
            }

        binding.uploadSocioClips.setOnClickListener {
            galleryImage.launch("video/*")
        }
        fun uploadPost(uri: Uri?, captions: String) {
            val reference =
                FirebaseDatabase.getInstance().getReference(SOCIO_CLIPS_NODE).child(userId!!)
                    .child(System.currentTimeMillis().toString())


            if (uri != null) {
                storage.getReference(USER_SOCIO_CLIPS_NODE).child(userId!!)
                    .child(System.currentTimeMillis().toString())
                    .putFile(uri)
                    .addOnSuccessListener { task ->
                        task.metadata?.reference?.downloadUrl
                            ?.addOnSuccessListener { selectedClip ->
                                val hashMap = hashMapOf(
                                    "uploaded_socio_clip_url" to "$selectedClip",
                                    "caption" to captions
                                )

                                reference.setValue(hashMap)
                                    .addOnCompleteListener {
                                        startActivity(Intent(this, SocioActivity::class.java))
                                        finish()
                                    }
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Post Upload not successfull!!", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }

        binding.postButton.setOnClickListener {

            val captions: String = binding.caption.text.toString()

            uploadPost(uri, captions)

        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this, SocioActivity::class.java))
            finish()
        }
    }
}