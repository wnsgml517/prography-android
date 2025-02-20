package com.android.prography.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.databinding.ItemBookmarkImageBinding
import com.bumptech.glide.Glide

class BookMarkImageAdapter : ListAdapter<PhotoResponse, BookMarkImageAdapter.BookmarkViewHolder>(
    DIFF_CALLBACK
) {
    private var onItemClickListener: ((PhotoResponse) -> Unit)? = null

    class BookmarkViewHolder(private val binding: ItemBookmarkImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoResponse, clickListener: ((PhotoResponse) -> Unit)?) {
            Glide.with(binding.ivBookmarkImage.context)
                .load(photo.imageUrls.regular)
                .into(binding.ivBookmarkImage)

            binding.root.setOnClickListener {
                clickListener?.invoke(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemBookmarkImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    fun setOnItemClickListener(listener: (PhotoResponse) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoResponse>() {
            override fun areItemsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}
