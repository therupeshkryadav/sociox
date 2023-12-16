package com.example.socialbundle.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialbundle.DataClasses.Posts
import com.example.socialbundle.R
import com.example.socialbundle.DataClasses.Stories
import com.example.socialbundle.databinding.FragmentHomeBinding
import com.example.socialbundle.Adapters.myAdapter
import com.example.socialbundle.Adapters.myAdapter2

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

        storiesList.add(Stories(R.drawable.profile_img, "Your Story"))


        postsList = ArrayList()
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username1","India","liked by ussername and 69 others","username1 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username2","India","liked by ussername and 69 others","username2 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username3","India","liked by ussername and 69 others","username3 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username4","India","liked by ussername and 69 others","username4 caption caption caption"))


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

