package com.android.prography.presentation.ui.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.databinding.FragmentHomeBinding
import com.android.prography.presentation.ui.adapter.BookMarkImageAdapter
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.util.HorizontalSpaceItemDecoration
import com.android.prography.presentation.util.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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
        }

        // ✅ 초기 로딩 시 Shimmer 활성화
        recentImageAdapter.setLoadingState(true)

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            Timber.i("photo : $photos")

            if (photos.isNotEmpty()) {
                recentImageAdapter.submitList(photos) // ✅ 데이터 로딩 완료 시 Shimmer 제거
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
}