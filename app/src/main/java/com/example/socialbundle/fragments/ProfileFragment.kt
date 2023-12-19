package com.example.socialbundle.fragments

import ViewPagerAdapter
import android.content.Intent
import android.graphics.drawable.Icon
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
import com.example.socialbundle.R
import com.example.socialbundle.databinding.FragmentProfileBinding
import com.example.socialbundle.Adapters.myAdapter3
import com.example.socialbundle.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var myAdapter3: myAdapter3
    private lateinit var highlightsList: ArrayList<Highlights>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize the ViewPagerAdapter
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)

        // Create an Icon instance using a drawable resource
        val iconResource0 = R.drawable.post_profile
        val icon0 = Icon.createWithResource(requireContext(), iconResource0)

        // Add the fragment and the icon to the adapter
        viewPagerAdapter.addFragment(MyPostsFragment(), icon0)

        // Create an Icon instance using a drawable resource
        val iconResource1 = R.drawable.socio_clips_logo
        val icon1 = Icon.createWithResource(requireContext(), iconResource1)
        // Add the fragment and the icon to the adapter
        viewPagerAdapter.addFragment(MyClipsFragment(), icon1)

        // Create an Icon instance using a drawable resource
        val iconResource2 = R.drawable.tag_profile
        val icon2 = Icon.createWithResource(requireContext(), iconResource2)
        // Add the fragment and the icon to the adapter
        viewPagerAdapter.addFragment(TaggedFragment(), icon2)

        // Set up your ViewPager or TabLayout with the adapter
        binding.viewPAger.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPAger)

        binding.tabLayout.getTabAt(0)?.setIcon(iconResource0)
        binding.tabLayout.getTabAt(1)?.setIcon(iconResource1)
        binding.tabLayout.getTabAt(2)?.setIcon(iconResource2)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        highlightsList = ArrayList()

        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.add_story, "Add Story"))

        binding.apply {
            logOut.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }

            editProfile.setOnClickListener {
                startActivity(Intent(requireContext(), EditActivity::class.java))
            }

            swipeRefreshLayout.setOnRefreshListener {
                // Handle the refresh action
                refreshProfile()
                swipeRefreshLayout.isRefreshing = false // Stop the loading animation
            }

            myAdapter3 = myAdapter3(highlightsList)
            rvHighlight.adapter = myAdapter3
            rvHighlight.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val current = FirebaseAuth.getInstance().currentUser

        databaseReference = FirebaseDatabase.getInstance().getReference(USER_NODE)
            .child(current?.uid ?: "defaultNode")


        // Read from the database
        databaseReference.get().addOnSuccessListener {
            if (it.exists()) {
                val userName = it.child("username").value
                val fullName = it.child("name").value
                val bIo = it.child(
                    "bio"
                ).value
                val iMageUrl = it.child("imageurl").value

                binding.username.text = userName.toString()
                binding.nicknameEdit.text = fullName.toString()
                binding.bio.text = bIo.toString()
                Picasso.get().load(iMageUrl.toString()).into(binding.imageProfile)


            } else {
                Toast.makeText(requireContext(), "No User Found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshProfile() {
        val current = FirebaseAuth.getInstance().currentUser

        databaseReference = FirebaseDatabase.getInstance().getReference(USER_NODE)
            .child(current?.uid ?: "defaultNode")


        // Read from the database
        databaseReference.get().addOnSuccessListener {
            if (it.exists()) {
                val userName = it.child("username").value
                val fullName = it.child("name").value
                val bIo = it.child("bio").value
                val iMageUrl = it.child("imageurl").value

                binding.username.text = userName.toString()
                binding.nicknameEdit.text = fullName.toString()
                binding.bio.text = bIo.toString()
                Picasso.get().load(iMageUrl.toString()).into(binding.imageProfile)


            } else {
                Toast.makeText(requireContext(), "No User Found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}



