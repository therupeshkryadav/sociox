package com.example.socialbundle.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialbundle.Adapter.MyAdapter
import com.example.socialbundle.R
import com.example.socialbundle.Stories
import com.example.socialbundle.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var myAdapter: MyAdapter
    private lateinit var storiesList: ArrayList<Stories>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        storiesList = ArrayList()

        storiesList.add(Stories(R.drawable.profile_img, "username1"))
        storiesList.add(Stories(R.drawable.profile_img, "username2"))
        storiesList.add(Stories(R.drawable.profile_img, "username3"))
        storiesList.add(Stories(R.drawable.profile_img, "username4"))
        storiesList.add(Stories(R.drawable.profile_img, "username5"))
        storiesList.add(Stories(R.drawable.profile_img, "username6"))
        storiesList.add(Stories(R.drawable.profile_img, "username7"))
        storiesList.add(Stories(R.drawable.profile_img, "username8"))
        storiesList.add(Stories(R.drawable.profile_img, "username9"))

        binding.apply {
            title.setOnClickListener {
                if (dropDownArrow.visibility == View.VISIBLE) {
                    dropDownArrow.visibility = View.GONE
                } else {
                    dropDownArrow.visibility = View.VISIBLE
                }
            }

            myAdapter = MyAdapter(storiesList)
            rvStory.adapter = myAdapter
            rvStory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        return binding.root
    }

}

