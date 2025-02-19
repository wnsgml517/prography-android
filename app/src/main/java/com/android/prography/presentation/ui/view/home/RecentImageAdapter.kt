package com.android.prography.presentation.ui.view.home
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.data.entity.RandomPhotoResponse
import com.android.prography.databinding.ItemRecentImageBinding
import com.bumptech.glide.Glide

class RecentImageAdapter : RecyclerView.Adapter<RecentImageAdapter.ViewHolder>() {

    private var itemList: List<RandomPhotoResponse> = emptyList()

    class ViewHolder(private val binding: ItemRecentImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RandomPhotoResponse) {
            binding.tvTitle.text = item.title ?: "No Title"  // ✅ `alt_description` 사용

            Glide.with(binding.root.context)
                .load(item.imageUrls.small)  // ✅ `imageUrls.small`을 로드
                .into(binding.rvRecentImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    fun submitList(newList: List<RandomPhotoResponse>) {
        itemList = newList
        notifyDataSetChanged()
    }
}
