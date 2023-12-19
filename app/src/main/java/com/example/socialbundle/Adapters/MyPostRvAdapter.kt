package com.example.socialbundle.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialbundle.Models.Post
import com.example.socialbundle.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class MyPostRvAdapter(var context:android.content.Context,var postList: ArrayList<Post>):RecyclerView.Adapter<MyPostRvAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MyPostRvDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= MyPostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.postingPost
        Picasso.get().load(postList.get(position).post_image_url).into(holder.binding.postingPost)
    }
}