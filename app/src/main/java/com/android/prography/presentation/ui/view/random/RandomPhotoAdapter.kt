package com.android.prography.presentation.ui.view.random

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.databinding.ItemRandomPhotoBinding
import com.bumptech.glide.Glide

class RandomPhotoAdapter :
    ListAdapter<PhotoResponse, RandomPhotoAdapter.PhotoViewHolder>(DIFF_CALLBACK) {

    private var onNotInterestedClickListener: ((PhotoResponse) -> Unit)? = null
    private var onInfoClickListener: ((PhotoResponse) -> Unit)? = null
    private var onBookmarkClickListener: ((PhotoResponse) -> Unit)? = null

    fun setOnNotInterestedClickListener(listener: (PhotoResponse) -> Unit) {
        onNotInterestedClickListener = listener
    }

    fun setOnInfoClickListener(listener: (PhotoResponse) -> Unit) {
        onInfoClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: (PhotoResponse) -> Unit) {
        onBookmarkClickListener = listener
    }

    class PhotoViewHolder(private val binding: ItemRandomPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoResponse, onNotInterested: ((PhotoResponse) -> Unit)?,
                 onInfo: ((PhotoResponse) -> Unit)?, onBookmark: ((PhotoResponse) -> Unit)?) {

            Glide.with(binding.ivBookmarkImage.context)
                .load(photo.imageUrls.regular)
                .into(binding.ivBookmarkImage)

            binding.ivNotInterestedButton.setOnClickListener {
                onNotInterested?.invoke(photo)
            }
            binding.ivInformationbutton.setOnClickListener {
                onInfo?.invoke(photo)
            }
            binding.ivBookMarkButton.setOnClickListener {
                onBookmark?.invoke(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemRandomPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position), onNotInterestedClickListener, onInfoClickListener, onBookmarkClickListener)
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
