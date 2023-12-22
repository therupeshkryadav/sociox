package com.example.socialbundle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialbundle.DataClasses.UploadSocioClips
import com.example.socialbundle.databinding.ClipsDesignBinding
import com.squareup.picasso.Picasso

class ClipsAdapter(var context: android.content.Context, var getSocioClipsList:ArrayList<UploadSocioClips>): RecyclerView.Adapter<ClipsAdapter.ViewHolder>()  {

    inner class ViewHolder(var binding: ClipsDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ClipsDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return getSocioClipsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(getSocioClipsList.get(position).profile_link).into(holder.binding.imageProfile)
        holder.binding.captionClips.setText(getSocioClipsList.get(position).caption)
        holder.binding.videoView.setVideoPath(getSocioClipsList.get(position).uploaded_socio_clip_url)
        holder.binding.videoView.setOnPreparedListener {
            it.start()
        }
    }
}