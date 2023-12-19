package com.example.socialbundle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialbundle.R
import com.example.socialbundle.DataClasses.Stories

class StoryAdapter(private val storiesList: ArrayList<Stories>) : RecyclerView.Adapter<StoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = storiesList[position]
        holder.profilePic.setImageResource(currentItem.profileImageStory)
        holder.username.text = currentItem.userName_story

    }

    override fun getItemCount(): Int {
        return storiesList.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val profilePic: ImageView = itemView.findViewById(R.id.profileImage)
        val username: TextView = itemView.findViewById(R.id.username_story)

    }

}

