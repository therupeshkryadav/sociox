package com.example.socialbundle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialbundle.MainActivity.Companion.auth
import com.example.socialbundle.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var myAdapter3: myAdapter3
    private lateinit var highlightsList: ArrayList<Highlights>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        highlightsList = ArrayList()

        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))
        highlightsList.add(Highlights(R.drawable.profile_img, "highlight"))


        binding.logOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.editProfile.setOnClickListener {
            startActivity(Intent(requireContext(),EditActivity::class.java))
        }

        myAdapter3 = myAdapter3(highlightsList)
        binding.rvHighlight.adapter = myAdapter3
        binding.rvHighlight.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        return binding.root
    }
}
