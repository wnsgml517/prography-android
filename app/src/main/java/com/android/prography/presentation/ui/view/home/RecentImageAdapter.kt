package com.android.prography.presentation.ui.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.databinding.ItemRecentImageBinding
import com.android.prography.databinding.ItemRecentImageShimmerBinding
import com.bumptech.glide.Glide
class RecentImageAdapter :
    ListAdapter<RecentPhotoResponse, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((RecentPhotoResponse) -> Unit)? = null

    companion object {
        private const val LOADING_VIEW_TYPE = 0
        private const val CONTENT_VIEW_TYPE = 1
        private const val SHIMMER_COUNT = 10 // ✅ 쉬머 개수

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecentPhotoResponse>() {
            override fun areItemsTheSame(oldItem: RecentPhotoResponse, newItem: RecentPhotoResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecentPhotoResponse, newItem: RecentPhotoResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return if (currentList.isEmpty()) SHIMMER_COUNT else currentList.size // ✅ 리스트가 비어있으면 쉬머 표시
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) LOADING_VIEW_TYPE else CONTENT_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADING_VIEW_TYPE) {
            val binding = ItemRecentImageShimmerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemRecentImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ContentViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentViewHolder && currentList.isNotEmpty()) {
            holder.bind(getItem(position), onItemClickListener)
        }
    }

    fun setOnItemClickListener(listener: (RecentPhotoResponse) -> Unit) {
        onItemClickListener = listener
    }

    // ✅ Shimmer ViewHolder
    class ShimmerViewHolder(binding: ItemRecentImageShimmerBinding) : RecyclerView.ViewHolder(binding.root)

    // ✅ 실제 데이터 ViewHolder
    class ContentViewHolder(private val binding: ItemRecentImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecentPhotoResponse, clickListener: ((RecentPhotoResponse) -> Unit)?) {
            binding.tvTitle.text = item.title ?: "No Title"

            Glide.with(binding.root.context)
                .load(item.imageUrls.small)
                .into(binding.rvRecentImage)

            binding.root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }
}
