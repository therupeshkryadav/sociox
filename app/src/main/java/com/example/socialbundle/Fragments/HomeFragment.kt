package com.example.socialbundle.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialbundle.Adapter.MyAdapter
import com.example.socialbundle.Stories
import com.example.socialbundle.databinding.FragmentHomeBinding
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private lateinit var storiesList: ArrayList<Stories>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myRecyclerView = binding.RecycleView
        myAdapter = MyAdapter(storiesList)
        myRecyclerView.adapter= myAdapter
        myRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.title.setOnClickListener{
            if(binding.dropDownArrow.visibility == View.VISIBLE)
            {
                binding.dropDownArrow.visibility = View.GONE
            }
            else{
                binding.dropDownArrow.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

}

