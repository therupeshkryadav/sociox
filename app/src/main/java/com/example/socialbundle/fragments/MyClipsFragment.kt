package com.example.socialbundle.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.socialbundle.DataClasses.UploadSocioClips
import com.example.socialbundle.adapters.UploadSocioClipAdapter
import com.example.socialbundle.databinding.FragmentMyClipsBinding
import com.example.socialbundle.utils.SOCIO_CLIPS_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyClipsFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var binding: FragmentMyClipsBinding
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyClipsBinding.inflate(inflater, container, false)
        val current = FirebaseAuth.getInstance().currentUser
        binding.rvUserSocioClips.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        databaseReference =
            FirebaseDatabase.getInstance().getReference(SOCIO_CLIPS_NODE).child(current!!.uid)

        databaseReference.get().addOnSuccessListener {
            if (it.exists()) {
                val socioclipsList = mutableListOf<UploadSocioClips>()
                for (childSnapshot in it.children) {
                    val post = childSnapshot.getValue(UploadSocioClips::class.java)
                    post?.let {
                        socioclipsList.add(it)
                    }
                }
                val adapter = UploadSocioClipAdapter(requireContext(), socioclipsList.toMutableList())
                adapter.notifyDataSetChanged()
                binding.rvUserSocioClips.adapter = adapter
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}