package com.android.prography.presentation.ui.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.databinding.FragmentHomeBinding
import com.android.prography.presentation.ui.adapter.BookMarkImageAdapter
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.ui.ext.DpToPx
import com.android.prography.presentation.ui.view.home.bookmark.ShimmerBookMarkAdapter
import com.android.prography.presentation.ui.view.home.recentImage.LoadingStateAdapter
import com.android.prography.presentation.ui.view.home.recentImage.ShimmerRecentImageAdapter
import com.android.prography.presentation.util.HorizontalSpaceItemDecoration
import com.android.prography.presentation.util.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {

    private lateinit var shimmerBookMarkAdapter: ShimmerBookMarkAdapter
    private lateinit var shimmerRecentImageAdapter: ShimmerRecentImageAdapter
    private lateinit var recentImageAdapter: RecentImageAdapter
    private lateinit var bookmarkImageAdapter: BookMarkImageAdapter
    private var lock : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // shimmerView 보이기
        initRecentImageSkeletonImage()

        // 아이템뷰 초기화
        initRecentImage()
        initBookmarkImage()

        // 클릭 리스너
        setItemClickListener()
    }

    private fun initRecentImageSkeletonImage()
    {
        // 최신 이미지
        shimmerRecentImageAdapter = ShimmerRecentImageAdapter()

        binding.rvShimmerView.apply {
            setHasFixedSize(false)
            addItemDecoration(SpacingItemDecoration(10.DpToPx()))

            // ✅ 초기에는 ShimmerAdapter 연결
            adapter = shimmerRecentImageAdapter

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
        viewLifecycleOwner.lifecycleScope.launch {
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

    private fun initBookmarkSkeletonImage()
    {
        // 최신 이미지
        shimmerBookMarkAdapter = ShimmerBookMarkAdapter()

        binding.rvShimmerBookmark.apply {
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = shimmerBookMarkAdapter
            addItemDecoration(HorizontalSpaceItemDecoration(10.DpToPx()))
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

                    // 데이터가 있을 경우, 스켈레톤 뷰 2초(예시) 와 북마크 text 표시
                    binding.tvBookmark.visibility = View.VISIBLE

                    initBookmarkSkeletonImage()
                    delay(2000)
                    // ✅ 1. Shimmer 애니메이션 멈추기
                    binding.rvShimmerBookmark.adapter = null

                    // ✅ 2. ShimmerView GONE 처리
                    binding.rvShimmerBookmark.visibility = View.GONE

                    // ✅ 3. 북마크 이미지 보여주기
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