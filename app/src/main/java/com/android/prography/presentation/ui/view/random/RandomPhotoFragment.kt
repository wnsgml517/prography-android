package com.android.prography.presentation.ui.view.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.prography.R
import com.android.prography.databinding.FragmentRandomPhotoBinding
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.ui.view.home.HomeViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.RewindAnimationSetting
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import com.yuyakaido.android.cardstackview.SwipeableMethod
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class RandomPhotoFragment : BaseFragment<FragmentRandomPhotoBinding, RandomPhotoViewModel>(
    FragmentRandomPhotoBinding::inflate,
    RandomPhotoViewModel::class.java
), CardStackListener {

    private lateinit var adapter: RandomPhotoAdapter
    private lateinit var layoutManager : CardStackLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RandomPhotoAdapter()
        layoutManager = CardStackLayoutManager(requireContext(), this).apply {
            setStackFrom(StackFrom.Top) // 카드가 위에서부터 쌓이도록 설정
            setVisibleCount(3) // 화면에 보여질 카드 개수
            setTranslationInterval(10.0f) // 카드 간 거리 조정
            setSwipeThreshold(0.2f) // 스와이프 감도 조정
            setMaxDegree(7.0f) // 🎯 회전 각도를 6.838도와 비슷하게 설정 (기본값: 20 → 7)
            setOverlayInterpolator(LinearInterpolator()) // 부드러운 애니메이션 적용
            setDirections(Direction.HORIZONTAL) // 좌/우 스와이프 가능
            setCanScrollHorizontal(true) // 가로 스와이프 활성화
            setCanScrollVertical(true) // 세로 스크롤 비활성화
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual) // 수동 & 자동 스와이프 가능
            setRewindAnimationSetting(
                RewindAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(DecelerateInterpolator())
                    .build()
            )
            setSwipeAnimationSetting(
                SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Slow.duration) // 애니메이션 속도를 조정 (느리게)
                    .setInterpolator(OvershootInterpolator(1.2f)) // 자연스럽게 튕기는 효과 추가
                    .build()
            )
        }

        binding.cvRandomView.apply {
            layoutManager = this@RandomPhotoFragment.layoutManager
            adapter = this@RandomPhotoFragment.adapter
            itemAnimator = DefaultItemAnimator()
        }

        // ✅ 어댑터의 클릭 리스너 설정
        adapter.setOnNotInterestedClickListener { photo ->
            // 왼쪽 스와이프
            swipeCard(Direction.Left)
        }


        adapter.setOnBookmarkClickListener { photo ->
            // 북마크 저장 후 오른쪽 스와이프
            viewModel.bookmarkPhoto(photo)
            swipeCard(Direction.Right)
        }

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            adapter.submitList(photos)
        }

        viewModel.fetchPhotos()
    }

    // ✅ 카드 스와이프 동작 실행
    private fun swipeCard(direction: Direction) {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(DecelerateInterpolator())
            .build()

        layoutManager.setSwipeAnimationSetting(setting)
        binding.cvRandomView.swipe()
    }


    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        val currentItem = adapter.currentList[layoutManager.topPosition - 1]

        when (direction) {
            Direction.Right -> {
                Timber.i("Bookmarking: ${currentItem.id}")
                viewModel.bookmarkPhoto(currentItem)
            }
            Direction.Left -> {
                Timber.i("Skipped: ${currentItem.id}")
            }
            else -> {}
        }
    }

    override fun onCardRewound() {}
    override fun onCardCanceled() {}
    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}
}