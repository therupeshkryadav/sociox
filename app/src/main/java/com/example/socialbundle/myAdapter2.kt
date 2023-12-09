package com.example.socialbundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class myAdapter2(private val postsList: ArrayList<Posts>) :
    RecyclerView.Adapter<myAdapter2.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = postsList[position]
        holder.profilepic.setImageResource(currentItem.profileImage)
        holder.postpic.setImageResource(currentItem.postImage)
        holder.username.text = currentItem.username
        holder.locations.text = currentItem.location
        holder.peopleslikedpost.text = currentItem.peoplesliked
        holder.userNDcaptions.text = currentItem.userNDcaption
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profilepic: ImageView = itemView.findViewById(R.id.profileImage_post)
        var postpic: ImageView = itemView.findViewById(R.id.postImage)
        var username: TextView = itemView.findViewById(R.id.username_post)
        var locations: TextView = itemView.findViewById(R.id.location)
        var peopleslikedpost: TextView = itemView.findViewById(R.id.peoplesLiked)
        var userNDcaptions: TextView = itemView.findViewById(R.id.usernameNdCaption)
    }

}
