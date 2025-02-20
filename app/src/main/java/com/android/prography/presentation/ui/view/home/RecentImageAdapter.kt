package com.android.prography.presentation.ui.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.data.entity.RandomPhotoResponse
import com.android.prography.databinding.ItemRecentImageBinding
import com.android.prography.databinding.ItemRecentImageShimmerBinding
import com.bumptech.glide.Glide

class RecentImageAdapter :
    ListAdapter<RandomPhotoResponse, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var isLoading = true // ✅ 로딩 상태 추가
    private var onItemClickListener: ((RandomPhotoResponse) -> Unit)? = null

    companion object {
        private const val LOADING_VIEW_TYPE = 0
        private const val CONTENT_VIEW_TYPE = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RandomPhotoResponse>() {
            override fun areItemsTheSame(oldItem: RandomPhotoResponse, newItem: RandomPhotoResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RandomPhotoResponse, newItem: RandomPhotoResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) LOADING_VIEW_TYPE else CONTENT_VIEW_TYPE
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
        if (holder is ContentViewHolder) {
            holder.bind(getItem(position), onItemClickListener)
        }
    }

    fun setOnItemClickListener(listener: (RandomPhotoResponse) -> Unit) {
        onItemClickListener = listener
    }

    // ✅ Shimmer ViewHolder
    class ShimmerViewHolder(binding: ItemRecentImageShimmerBinding) : RecyclerView.ViewHolder(binding.root)

    // ✅ 실제 데이터 ViewHolder
    class ContentViewHolder(private val binding: ItemRecentImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RandomPhotoResponse, clickListener: ((RandomPhotoResponse) -> Unit)?) {
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
