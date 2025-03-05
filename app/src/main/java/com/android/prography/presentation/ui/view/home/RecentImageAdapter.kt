package com.android.prography.presentation.ui.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.databinding.ItemRecentImageBinding
import com.android.prography.databinding.ItemRecentImageShimmerBinding
import com.bumptech.glide.Glide

class RecentImageAdapter :
    PagingDataAdapter<RecentPhotoResponse, RecyclerView.ViewHolder>(RecentDiffCallback()) {

    private val ITEM_VIEW_TYPE = 0
    private val LOADING_VIEW_TYPE = 1

    private var onItemClickListener: ((RecentPhotoResponse) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return if (snapshot().items.get(0).id.isBlank()) LOADING_VIEW_TYPE else ITEM_VIEW_TYPE
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
        if (holder is ContentViewHolder && snapshot().items.get(0).id.isNotBlank()) {
            getItem(position)?.let { holder.bind(it, onItemClickListener) }
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

// DiffCallback 설정 (데이터 변경 감지)
class RecentDiffCallback : DiffUtil.ItemCallback<RecentPhotoResponse>() {
    override fun areItemsTheSame(
        oldItem: RecentPhotoResponse,
        newItem: RecentPhotoResponse
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: RecentPhotoResponse,
        newItem: RecentPhotoResponse
    ): Boolean {
        return oldItem == newItem
    }
}