package com.example.socialbundle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialbundle.DataClasses.UploadSocioClips
import com.example.socialbundle.databinding.MySocioClipsRvDesigningBinding

class UploadSocioClipAdapter(
    var context: android.content.Context,
    private var getSocioClipsList: List<UploadSocioClips>
) : RecyclerView.Adapter<UploadSocioClipAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MySocioClipsRvDesigningBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MySocioClipsRvDesigningBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return getSocioClipsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(getSocioClipsList[position].uploaded_socio_clip_url).diskCacheStrategy(
            DiskCacheStrategy.ALL)
            .into(holder.binding.postingPost)
    }
}