package com.example.socialbundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class myAdapter3(private val highlightList: ArrayList<Highlights>) : RecyclerView.Adapter<myAdapter3.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.highlight_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = highlightList[position]
        holder.highlightPic.setImageResource(currentItem.highlightIMage)
        holder.highlightTag.text = currentItem.highlightTAG

    }

    override fun getItemCount(): Int {
        return highlightList.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val highlightPic: ImageView = itemView.findViewById(R.id.highlightImage)
        val highlightTag: TextView = itemView.findViewById(R.id.highlight_tag)


    }

}