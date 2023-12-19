package com.example.socialbundle.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.socialbundle.adapters.UserPostsAdapter
import com.example.socialbundle.DataClasses.AddPost
import com.example.socialbundle.databinding.FragmentMyPostsBinding
import com.example.socialbundle.utils.POST_NODE
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class MyPostsFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var binding: FragmentMyPostsBinding
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
                    post?.let { addPostList.add(it) }
                }
                val adapter = UserPostsAdapter(requireContext(), addPostList.toMutableList())
                binding.rvUserPosts.adapter = adapter
            } else {
                Toast.makeText(requireContext(), "No User Found", Toast.LENGTH_SHORT).show()
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