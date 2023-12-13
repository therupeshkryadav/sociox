package com.example.socialbundle.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialbundle.EditActivity
import com.example.socialbundle.Highlights
import com.example.socialbundle.LoginActivity
import com.example.socialbundle.MainActivity.Companion.auth
import com.example.socialbundle.R
import com.example.socialbundle.databinding.FragmentProfileBinding
import com.example.socialbundle.Adapters.myAdapter3
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var myAdapter3: myAdapter3
    private lateinit var highlightsList: ArrayList<Highlights>
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        highlightsList = ArrayList()

        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.add_story, "Add Story"))

        binding.apply {
            logOut.setOnClickListener {
                auth.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }

            editProfile.setOnClickListener {
                startActivity(Intent(requireContext(), EditActivity::class.java))
            }

            myAdapter3 = myAdapter3(highlightsList)
            rvHighlight.adapter = myAdapter3
            rvHighlight.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val current = auth.currentUser

        databaseReference = FirebaseDatabase.getInstance().getReference(USER_NODE).child(current?.uid ?: "defaultNode")


        // Read from the database
        databaseReference.get().addOnSuccessListener{
            if(it.exists()){
                val userName = it.child("username").value
                val fullName = it.child("name").value
                val bIo = it.child("emailId").value
                val iMageUrl = it.child("imageurl").value

                binding.username.text=userName.toString()
                binding.nicknameEdit.text=fullName.toString()
                binding.bio.text=bIo.toString()
                Picasso.get().load(iMageUrl.toString()).into(binding.imageProfile)


            }else{
                Toast.makeText(requireContext(),"No User Found",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
        }


            return binding.root
        }

    override fun onResume() {
        super.onResume()
        highlightsList = ArrayList()

        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.add_story, "Add Story"))

        binding.apply {
            logOut.setOnClickListener {
                auth.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }

            editProfile.setOnClickListener {
                startActivity(Intent(requireContext(), EditActivity::class.java))
            }

            myAdapter3 = myAdapter3(highlightsList)
            rvHighlight.adapter = myAdapter3
            rvHighlight.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val current = auth.currentUser

        databaseReference = FirebaseDatabase.getInstance().getReference(USER_NODE).child(current?.uid ?: "defaultNode")


        // Read from the database
        databaseReference.get().addOnSuccessListener{
            if(it.exists()){
                val userName = it.child("username").value
                val fullName = it.child("name").value
                val bIo = it.child("bio").value
                val iMageUrl = it.child("imageurl").value

                binding.username.text=userName.toString()
                binding.nicknameEdit.text=fullName.toString()
                binding.bio.text=bIo.toString()
                binding.bio.text=bIo.toString()
                Picasso.get().load(iMageUrl.toString()).into(binding.imageProfile)


            }else{
                Toast.makeText(requireContext(),"No User Found",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
        }

    }
    }


