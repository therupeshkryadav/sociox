package com.example.socialbundle.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MyPostsFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var binding: FragmentMyPostsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        val current = FirebaseAuth.getInstance().currentUser
        val postList = ArrayList<AddPost>()
        val adapter = UserPostsAdapter(requireContext(), postList)
        binding.rvUserPosts.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)


        databaseReference =
            FirebaseDatabase.getInstance().getReference(USER_NODE).child(current!!.uid)


        binding.rvUserPosts.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.apply {

        }
    }
}