package com.android.prography.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.R
import com.bumptech.glide.Glide

class RecentImageAdapter(private val itemList: List<ImageItem>) :
    RecyclerView.Adapter<RecentImageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.rv_recent_image)
        val titleText: TextView = view.findViewById(R.id.tv_recent_title)
        val subText: TextView = view.findViewById(R.id.tv_sub_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleText.text = item.title
        holder.subText.text = item.subtitle
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int = itemList.size
}
