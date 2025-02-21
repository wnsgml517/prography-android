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
import com.android.prography.presentation.ui.ext.DpToPx
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
    private var lock : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecentImage()
        initBookmarkImage()
        setItemClickListener()
    }

    private fun initRecentImage() {
        recentImageAdapter = RecentImageAdapter()

        // 최신 이미지 초기 셋팅
        binding.rvRecentImage.apply {
            setHasFixedSize(false)
            isNestedScrollingEnabled = false
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE // ✅ 간격 문제 해결
            }
            adapter = recentImageAdapter

            // ✅ 수정된 간격 적용 (위/아래/왼쪽/오른쪽 균등)
            addItemDecoration(SpacingItemDecoration(10.DpToPx()))

            // ✅ 무한 스크롤 리스너 적용
            initInfiniteScroll()
        }


        // 최신 이미지 읽어옴
        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            Timber.i("photo : $photos")
            stopLoading()

            if (photos.isNotEmpty()) {
                val recyclerViewState = binding.rvRecentImage.layoutManager?.onSaveInstanceState()
                recentImageAdapter.submitList(photos) {
                    binding.rvRecentImage.layoutManager?.onRestoreInstanceState(recyclerViewState)

                    // ✅ StaggeredGridLayoutManager 강제 리레이아웃
                    binding.rvRecentImage.post {
                        binding.rvRecentImage.invalidateItemDecorations() // ✅ 간격 재조정
                        binding.rvRecentImage.requestLayout() // ✅ 새로 배치
                    }
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

    private fun initInfiniteScroll() {
        binding.rvRecentImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy <= 0 || lock) return // ✅ 위로 스크롤할 때는 무시

                // ✅ 스크롤이 마지막 부분까지 갔는지 확인하는 로직
                val recyclerViewHeight = recyclerView.height
                val scrollExtent = recyclerView.computeVerticalScrollExtent() // 현재 보이는 RecyclerView 영역 크기
                val scrollOffset = recyclerView.computeVerticalScrollOffset() // 스크롤된 거리
                val scrollRange = recyclerView.computeVerticalScrollRange() // RecyclerView 전체 크기

                // ✅ 더 이상 스크롤할 영역이 없을 때 새로운 데이터 요청
                if (scrollOffset + scrollExtent >= scrollRange - 10) { // 💡 마지막 10px 여백까지 고려
                    lock = true
                    startLoading()
                    viewModel.fetchPhotos()
                }
            }
        })
    }

    // ✅ 로딩 시작 (애니메이션 실행)
    fun startLoading() = with(binding) {
        clLoadingBar.visibility = View.VISIBLE // 보이게 설정
        lottieLoader.playAnimation() // 애니메이션 실행
    }

    // ✅ 로딩 종료 (애니메이션 정지)
    fun stopLoading() = with(binding) {
        lottieLoader.cancelAnimation() // 애니메이션 멈춤
        clLoadingBar.visibility = View.GONE // 숨김
        lock = false
    }
}