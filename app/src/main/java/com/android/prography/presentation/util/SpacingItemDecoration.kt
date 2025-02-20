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
        val position = parent.getChildAdapterPosition(view) // 현재 아이템 위치
        val layoutManager = parent.layoutManager as? StaggeredGridLayoutManager
        val spanCount = layoutManager?.spanCount ?: 2 // 열 개수 (기본 2)

        if (position == RecyclerView.NO_POSITION) return

        // ✅ 첫 번째 줄 아이템: 위쪽 간격을 0으로 설정
        if (position == spanCount) {
            outRect.top = 0
        } else {
            outRect.top = spacing // 나머지는 기본 간격 적용
        }

        outRect.bottom = spacing // ✅ 모든 아이템에 아래 간격 추가

        // ✅ 첫 번째 열 아이템은 왼쪽 여백 없음
        if (position % spanCount == 0) {
            outRect.left = 0
            outRect.right = spacing / 2
        }
        // ✅ 마지막 열 아이템은 오른쪽 여백 없음
        else {
            outRect.left = spacing / 2
            outRect.right = 0
        }
    }
}
