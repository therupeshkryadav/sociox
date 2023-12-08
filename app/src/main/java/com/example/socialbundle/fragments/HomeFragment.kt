package com.example.socialbundle.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.socialbundle.Posts
import com.example.socialbundle.R
import com.example.socialbundle.Stories
import com.example.socialbundle.adapter.myAdapter
import com.example.socialbundle.adapter.myAdapter2
import com.example.socialbundle.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var myAdapter: myAdapter
    private lateinit var myAdapter2: myAdapter2
    private lateinit var storiesList: ArrayList<Stories>
    private lateinit var postsList: ArrayList<Posts>


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

        postsList = ArrayList()
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"liked by username and 69 others","username caption caption caption caption caption caption caption","username","India"))


        binding.apply {
            title.setOnClickListener {
                if (dropDownArrow.visibility == View.VISIBLE) {
                    dropDownArrow.visibility = View.GONE
                } else {
                    dropDownArrow.visibility = View.VISIBLE
                }
            }

            myAdapter = myAdapter(storiesList)
            rvStory.adapter = myAdapter
            rvStory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            myAdapter2 = myAdapter2(postsList)
            rvPost.adapter = myAdapter2
            rvPost.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        }

        return binding.root
    }

}

