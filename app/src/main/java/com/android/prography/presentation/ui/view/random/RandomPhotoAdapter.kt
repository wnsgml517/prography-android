package com.android.prography.presentation.ui.view.random
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.databinding.ItemRandomPhotoBinding
import com.bumptech.glide.Glide

class RandomPhotoAdapter : ListAdapter<PhotoResponse, RandomPhotoAdapter.PhotoViewHolder>(
    DIFF_CALLBACK
) {

    class PhotoViewHolder(private val binding: ItemRandomPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoResponse) {
            Glide.with(binding.ivBookmarkImage.context)
                .load(photo.imageUrls.regular)
                .into(binding.ivBookmarkImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemRandomPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position)) // getItem() 사용
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoResponse>() {
            override fun areItemsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem.id == newItem.id // ID가 같은지 비교
            }

            override fun areContentsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem == newItem // 내용이 같은지 비교
            }
        }
    }
}
