package com.example.socialbundle.UploadingWork

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbundle.activities.SocioActivity
import com.example.socialbundle.databinding.ActivityPostingBinding
import com.example.socialbundle.fragments.HomeFragment
import com.example.socialbundle.fragments.ProfileFragment
import com.example.socialbundle.utils.POST_IMAGES_NODE
import com.example.socialbundle.utils.POST_NODE
import com.example.socialbundle.utils.USER_IMAGES_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class PostingActivity : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private var uri: Uri? = null
    private lateinit var binding: ActivityPostingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingBinding.inflate(layoutInflater)
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
            startActivity(Intent(this,SocioActivity::class.java))
            finish()
        }
        val galleryImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) { imageuri ->
                binding.postImageAndVideo.setImageURI(imageuri)
                uri = imageuri
            }

        binding.postImageAndVideo.setOnClickListener {
            galleryImage.launch("image/*")
        }
        fun uploadPost(uri: Uri?,captions:String) {
            val reference = FirebaseDatabase.getInstance().getReference(POST_NODE).child(userId!!).child(System.currentTimeMillis().toString())

            if (uri != null) {
                storage.getReference(USER_IMAGES_NODE).child(POST_IMAGES_NODE)
                    .child(userId!!).child(System.currentTimeMillis().toString())
                    .putFile(uri)
                    .addOnSuccessListener { task ->
                        task.metadata?.reference?.downloadUrl
                            ?.addOnSuccessListener { selectedImage ->
                                val hashMap = hashMapOf(
                                    "post_image_url" to "$selectedImage",
                                    "caption" to captions
                                )
                                reference.setValue(hashMap)
                                    .addOnCompleteListener {
                                        startActivity(Intent(this,SocioActivity::class.java))
                                        finish()
                                    }
                            }
                    }
                    .addOnFailureListener {
                        binding.progressPost.visibility= View.INVISIBLE
                        Toast.makeText(this, "Post Upload not successfull!!", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
        binding.postButton.setOnClickListener {

            binding.progressPost.visibility= View.VISIBLE

            val captions: String = binding.caption.text.toString()

            uploadPost(uri,captions)

        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this,SocioActivity::class.java))
            finish()
        }
    }
}