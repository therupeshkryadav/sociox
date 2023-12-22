package com.example.socialbundle.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialbundle.DataClasses.UploadSocioClips
import com.example.socialbundle.adapters.ClipsAdapter
import com.example.socialbundle.databinding.FragmentClipsBinding
import com.example.socialbundle.utils.SOCIO_CLIPS_NODE
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ClipsFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentClipsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClipsBinding.inflate(inflater, container, false)
        databaseReference =
            FirebaseDatabase.getInstance().getReference(SOCIO_CLIPS_NODE)

        databaseReference.get().addOnSuccessListener {
            if (it.exists()) {
                var clipslist = ArrayList<UploadSocioClips>()
                for (childSnapshot in it.children) {
                    val clips = childSnapshot.getValue(UploadSocioClips::class.java)
                    clips?.let {
                        clipslist.add(it)
                    }
                }

                val adapter = ClipsAdapter(requireContext(), clipslist)
                binding.viewPager.adapter = adapter
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }
}