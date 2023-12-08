package com.example.socialbundle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialbundle.Posts
import com.example.socialbundle.R

class myAdapter2(private val postsList: ArrayList<Posts>) : RecyclerView.Adapter<myAdapter2.MyViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder2 {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return MyViewHolder2(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        val currentItem = postsList[position]
        holder.profilePic.setImageResource(currentItem.profileImage)
        holder.postPic.setImageResource(currentItem.postImage)
        holder.username.text = currentItem.username
        holder.usernameNDcaption.text = currentItem.userNDcaption
        holder.locations.text = currentItem.location
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    class MyViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: ImageView = itemView.findViewById(R.id.profileImage_post)
        val postPic: ImageView = itemView.findViewById(R.id.postImage)
        val username: TextView = itemView.findViewById(R.id.username_post)
        val usernameNDcaption: TextView = itemView.findViewById(R.id.usernameNdCaption)
        val locations: TextView = itemView.findViewById(R.id.location)

    }
}