package com.android.prography.presentation.ui.view.home.recentImage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.prography.databinding.ItemRecentImageShimmerBinding

class ShimmerRecentImageAdapter : RecyclerView.Adapter<ShimmerRecentImageAdapter.ShimmerViewHolder>() {

    // ✅ 항상 10개의 아이템을 보여주기
    override fun getItemCount(): Int = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val binding = ItemRecentImageShimmerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        // ❌ 데이터 바인딩 필요 없음 (그냥 Shimmer만 보여줌)

    }

    class ShimmerViewHolder(binding: ItemRecentImageShimmerBinding) :
        RecyclerView.ViewHolder(binding.root)
}
