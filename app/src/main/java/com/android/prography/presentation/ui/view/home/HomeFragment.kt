package com.android.prography.presentation.ui.view.home

import android.os.Bundle
import android.text.Layout.Directions
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.databinding.FragmentHomeBinding
import com.android.prography.presentation.ui.adapter.BookMarkImageAdapter
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.ui.ext.DpToPx
import com.android.prography.presentation.ui.view.home.recentImage.LoadingStateAdapter
import com.android.prography.presentation.ui.view.home.recentImage.ShimmerAdapter
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

    private lateinit var shimmerAdapter: ShimmerAdapter
    private lateinit var recentImageAdapter: RecentImageAdapter
    private lateinit var bookmarkImageAdapter: BookMarkImageAdapter
    private var lock : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSkeletonImage()
        initRecentImage()
        initBookmarkImage()
        setItemClickListener()
    }

    private fun initSkeletonImage()
    {
        shimmerAdapter = ShimmerAdapter()

        binding.rvShimmerView.apply {
            setHasFixedSize(false)
            addItemDecoration(SpacingItemDecoration(10.DpToPx()))

            // ✅ 초기에는 ShimmerAdapter 연결
            adapter = shimmerAdapter

            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
        }

    }

    private fun initRecentImage() {
        recentImageAdapter = RecentImageAdapter()

        binding.rvRecentImage.apply {
            setHasFixedSize(false)
            addItemDecoration(SpacingItemDecoration(10.DpToPx()))

            // ✅ Lottie 로딩바 적용
            adapter = recentImageAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { recentImageAdapter.retry() }
            )

            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
        }

        // ✅ 페이징 데이터 바인딩
        lifecycleScope.launch {
            viewModel.recentPhotosFlow.collectLatest { pagingData ->
                val layoutManager = binding.rvRecentImage.layoutManager as StaggeredGridLayoutManager

                // ✅ 기존 스크롤 위치 저장
                val previousPosition = layoutManager.findFirstVisibleItemPositions(null)

                recentImageAdapter.submitData(pagingData)

                // ✅ 기존 배치를 유지하면서 새로운 데이터 추가
                layoutManager.invalidateSpanAssignments()

                // ✅ 기존 위치 유지 (스크롤을 리셋하지 않도록 설정)
                if (previousPosition.isNotEmpty()) {
                    binding.rvRecentImage.scrollToPosition(previousPosition[0])
                }
            }
        }

        // ✅ 데이터 로딩 상태 감지해서 부드럽게 전환 & Shimmer 종료
        lifecycleScope.launch {
            recentImageAdapter.loadStateFlow.collectLatest { loadStates ->
                val isLoading = loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
                if (!isLoading) {
                    // ✅ 1. Shimmer 애니메이션 멈추기
                    binding.rvShimmerView.adapter = null

                    // ✅ 2. ShimmerView GONE 처리
                    binding.rvShimmerView.visibility = View.GONE
                }
            }
        }
    }


    private fun initBookmarkImage() {
        bookmarkImageAdapter = BookMarkImageAdapter()

        binding.rvBookmark.apply {
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bookmarkImageAdapter
            addItemDecoration(HorizontalSpaceItemDecoration(10.DpToPx()))
        }

        // ViewModel에서 북마크된 이미지 수집
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookmarkedPhotos.collectLatest { photos ->
                Timber.i("북마크된 이미지: $photos")

                if (photos.isNotEmpty()) {
                    val convertedList = photos.map { bookmark ->
                        PhotoResponse(id = bookmark.id, imageUrls = ImageUrls(bookmark.imageUrl.small, bookmark.imageUrl.regular))
                    }
                    bookmarkImageAdapter.submitList(convertedList)
                }
                else
                {
                    binding.tvBookmark.visibility = View.GONE
                    Timber.i("불러올 이미지가 없습니다.")
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
}