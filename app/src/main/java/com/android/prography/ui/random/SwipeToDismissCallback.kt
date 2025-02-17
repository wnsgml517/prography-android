package com.android.prography.ui.random

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class SwipeToDismissCallback(private val adapter: RecyclerView.Adapter<*>) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        // 아이템 제거
        (adapter as? RandomPhotoAdapter)?.apply {
            notifyItemRemoved(position)
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.3f // 30% 정도 스와이프하면 날아가도록 설정
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val view = viewHolder.itemView

        // 스와이프 비율 계산
        val ratio = dX / recyclerView.width
        val rotation = ratio * 20f  // 카드가 최대 20도 회전하도록 설정

        // 카드 기울이기 및 위로 이동 애니메이션 적용
        view.rotation = rotation
        view.translationY = -abs(ratio) * 300 // 위로 날아가는 효과

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
