package com.android.prography.presentation.ui.view.home

import android.os.Bundle
import android.text.Layout.Directions
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.databinding.FragmentHomeBinding
import com.android.prography.presentation.ui.adapter.BookMarkImageAdapter
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.util.HorizontalSpaceItemDecoration
import com.android.prography.presentation.util.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.log

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {
    private lateinit var recentImageAdapter: RecentImageAdapter
    private lateinit var bookmarkImageAdapter: BookMarkImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecentImage()
        initBookmarkImage()
        setItemClickListener()
    }

    private fun initRecentImage() {
        recentImageAdapter = RecentImageAdapter()

        binding.rvRecentImage.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE // ✅ 간격 문제 해결
            }
            adapter = recentImageAdapter

            // ✅ 수정된 간격 적용 (위/아래/왼쪽/오른쪽 균등)
            addItemDecoration(SpacingItemDecoration(10))

            // ✅ 무한 스크롤 리스너 적용
            initInfiniteScroll()
        }

        // ✅ 초기 로딩 시 Shimmer 활성화
        recentImageAdapter.setLoadingState(true)

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            Timber.i("photo : $photos")

            // ✅ 데이터 로딩 완료 시 Shimmer 제거
            recentImageAdapter.setLoadingState(false)

            if (photos.isNotEmpty()) {
                val recyclerViewState = binding.rvRecentImage.layoutManager?.onSaveInstanceState()
                recentImageAdapter.submitList(photos) {
                    binding.rvRecentImage.layoutManager?.onRestoreInstanceState(recyclerViewState)
                }
            }
        }
    }


    private fun initBookmarkImage() {
        bookmarkImageAdapter = BookMarkImageAdapter()

        binding.rvBookmark.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bookmarkImageAdapter
            addItemDecoration(HorizontalSpaceItemDecoration(10))
        }

        // ViewModel에서 북마크된 이미지 수집
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookmarkedPhotos.collectLatest { photos ->
                delay(1000) // 1초 로딩 시간
                stopLoading()
                Timber.i("북마크된 이미지: $photos")

                if (photos.isNotEmpty()) {
                    val convertedList = photos.map { bookmark ->
                        PhotoResponse(id = bookmark.id, imageUrls = ImageUrls(bookmark.imageUrl.small, bookmark.imageUrl.regular))
                    }
                    bookmarkImageAdapter.submitList(convertedList)
                }
                else
                {
                    binding.rvBookmark.visibility = View.GONE
                    binding.tvBookmark.visibility = View.GONE
                }
            }
        }
    }
    private fun setItemClickListener() {
        // ✅ 최신 이미지 클릭 시 Detail 화면으로 이동
        recentImageAdapter.setOnItemClickListener { photo ->
            goToDetailFragment(photo.id, photo.imageUrls)
        }
        // ✅ 북마크 클릭 시 Detail 화면으로 이동
        bookmarkImageAdapter.setOnItemClickListener { photo ->
            goToDetailFragment(photo.id, photo.imageUrls)
        }
    }

    // ✅ 공통으로 DetailFragment 이동 메서드
    private fun goToDetailFragment(id: String, imageUrl: ImageUrls) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetail(
            id = id,
            smallUrl = imageUrl.small,
            regularUrl = imageUrl.regular
        )
        findNavController().navigate(action)
    }

    private fun initInfiniteScroll() {
        binding.rvRecentImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy <= 0) return // ✅ 위로 스크롤할 때는 무시

                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItems = layoutManager.findLastVisibleItemPositions(null) // ✅ 마지막으로 보이는 아이템 위치 가져오기

                startLoading()

                if (lastVisibleItems.isNotEmpty()) {
                    val lastVisibleItemPosition = lastVisibleItems.maxOrNull() ?: 0 // ✅ 가장 큰 값을 가져오기 (즉, 가장 아래쪽 아이템)

                    // ✅ 마지막 아이템이 보이면 추가 데이터 요청
                    if (!viewModel.isLoading.value!! && lastVisibleItemPosition >= totalItemCount - 2) {
                        viewModel.fetchPhotos()
                    }
                }
            }
        })
    }
    // ✅ 로딩 시작 (애니메이션 실행)
    fun startLoading() = with(binding) {
        lottieLoader.visibility = View.VISIBLE // 보이게 설정
        lottieLoader.playAnimation() // 애니메이션 실행
    }

    // ✅ 로딩 종료 (애니메이션 정지)
    fun stopLoading() = with(binding) {
        lottieLoader.cancelAnimation() // 애니메이션 멈춤
        lottieLoader.visibility = View.GONE // 숨김
    }
}