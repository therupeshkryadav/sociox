package com.example.socialbundle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialbundle.DataClasses.AddPost
import com.example.socialbundle.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class UserPostsAdapter(
    var context: android.content.Context,
    private var getAddpostList: List<AddPost>
) : RecyclerView.Adapter<UserPostsAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MyPostRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: UserPostsAdapter.ViewHolder, position: Int) {
        Picasso.get().load(getAddpostList[position].post_image_url).into(holder.binding.postingPost)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserPostsAdapter.ViewHolder {
        val binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return getAddpostList.size
    }
}