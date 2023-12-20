package com.example.socialbundle.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.socialbundle.adapters.UploadSocioClipAdapter
import com.example.socialbundle.DataClasses.AddPost
import com.example.socialbundle.adapters.UserPostsAdapter
import com.example.socialbundle.databinding.FragmentMyPostsBinding
import com.example.socialbundle.utils.POST_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyPostsFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var binding: FragmentMyPostsBinding
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        val current = FirebaseAuth.getInstance().currentUser
        binding.rvUserPosts.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        databaseReference =
            FirebaseDatabase.getInstance().getReference(POST_NODE).child(current!!.uid)

        databaseReference.get().addOnSuccessListener {
            if (it.exists()) {
                val addPostList = mutableListOf<AddPost>()
                for (childSnapshot in it.children) {
                    val post = childSnapshot.getValue(AddPost::class.java)
                    post?.let {
                        addPostList.add(it)
                    }
                }
                val adapter = UserPostsAdapter(requireContext(), addPostList.toMutableList())
                adapter.notifyDataSetChanged()
                binding.rvUserPosts.adapter = adapter
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.apply {

        }
    }
}