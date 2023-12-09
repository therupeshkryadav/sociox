package com.example.socialbundle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username1","India","liked by ussername and 69 others","username1 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username2","India","liked by ussername and 69 others","username2 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username3","India","liked by ussername and 69 others","username3 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username4","India","liked by ussername and 69 others","username4 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username5","India","liked by ussername and 69 others","username5 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username6","India","liked by ussername and 69 others","username6 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username7","India","liked by ussername and 69 others","username7 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username8","India","liked by ussername and 69 others","username8 caption caption caption"))
        postsList.add(Posts(R.drawable.profile_img, R.drawable.first,"username9","India","liked by ussername and 69 others","username9 caption caption caption"))

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

