package com.android.prography.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val layoutParams = view.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        val spanIndex = layoutParams?.spanIndex ?: 0 // ✅ 현재 아이템이 몇 번째 열(Span)에 있는지 확인

        outRect.bottom = spacing // ✅ 모든 아이템 아래 간격 추가

        if (spanIndex == 0) { // ✅ 첫 번째 열
            outRect.left = 0
            outRect.right = spacing / 2
        } else { // ✅ 두 번째 열
            outRect.left = spacing / 2
            outRect.right = 0
        }
    }
}
