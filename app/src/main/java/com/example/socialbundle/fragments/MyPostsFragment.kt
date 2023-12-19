package com.example.socialbundle.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.socialbundle.Adapters.MyPostRvAdapter
import com.example.socialbundle.Models.Post
import com.example.socialbundle.databinding.FragmentMyPostsBinding
import com.example.socialbundle.utils.POST_NODE
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyPostsFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var binding: FragmentMyPostsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        val current = FirebaseAuth.getInstance().currentUser
        val postList = ArrayList<Post>()
        val adapter = MyPostRvAdapter(requireContext(), postList)
        binding.rvUserPosts.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)


        databaseReference =
            FirebaseDatabase.getInstance().getReference(USER_NODE).child(current!!.uid)
                .child(POST_NODE)
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tempList =
                        ArrayList<Post>() // Create a local list to avoid potential data duplication
                    for (postSnapshot in snapshot.children) {
                        val post: Post? = postSnapshot.getValue(Post::class.java)
                        post?.let { value ->
                            tempList.add(value)
                            Log.d("Posdidlektj", "$value ,, ${snapshot.value}")
                        }
                    }
                    postList.addAll(tempList)
                    // Notify the adapter after the loop to avoid unnecessary notifications
                    binding.rvUserPosts.adapter?.notifyDataSetChanged()
                } else {
                    // Handle the case when no data is available
                    // You might want to clear the existing postList if needed
                    postList.clear()
                    // Notify the adapter that the data set has changed
                    binding.rvUserPosts.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MyPostsFragment", "Error loading posts", error.toException())
                // Handle the error as needed, you might want to show a message to the user
                // or take appropriate action based on the error
            }
        })

        binding.rvUserPosts.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.apply {

        }
    }
}