package com.example.socialbundle.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.socialbundle.UploadingWork.PostingActivity
import com.example.socialbundle.UploadingWork.UploadSocioClipsActivity
import com.example.socialbundle.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            postAdd.setOnClickListener {
                activity?.startActivity(Intent(requireContext(), PostingActivity::class.java))
            }
            socioClips.setOnClickListener {
                activity?.startActivity(Intent(requireContext(), UploadSocioClipsActivity::class.java))
            }
        }
    }

}