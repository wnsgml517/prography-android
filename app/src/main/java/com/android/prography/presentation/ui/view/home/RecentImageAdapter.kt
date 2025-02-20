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

    private var isLoading = true // ✅ 로딩 상태 추가
    private var onItemClickListener: ((RecentPhotoResponse) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong() + position // ✅ **포지션을 추가해 중복 방지**
    }

    companion object {
        private const val LOADING_VIEW_TYPE = 0
        private const val CONTENT_VIEW_TYPE = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecentPhotoResponse>() {
            override fun areItemsTheSame(oldItem: RecentPhotoResponse, newItem: RecentPhotoResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecentPhotoResponse, newItem: RecentPhotoResponse): Boolean {
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
