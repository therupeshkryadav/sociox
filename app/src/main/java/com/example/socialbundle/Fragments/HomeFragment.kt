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

        storiesList.add(Stories(R.drawable.crown, "Rupa Randi"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Chakka"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Mehri"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Chinar"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Chudakkad"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Pelakkad"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Ladchati"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Matar"))
        storiesList.add(Stories(R.drawable.crown, "Rupa Gay"))

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

