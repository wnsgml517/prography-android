package com.android.prography.presentation.ui.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.prography.databinding.FragmentHomeBinding
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.util.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {
    private lateinit var recentImageAdapter: RecentImageAdapter

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
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS // ✅ 간격 문제 해결
            }
            adapter = recentImageAdapter

            // ✅ 수정된 간격 적용 (위/아래/왼쪽/오른쪽 균등)
            addItemDecoration(SpacingItemDecoration(20))
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
        val itemList = listOf(
            ImageItem("https://source.unsplash.com/random/200x200", "이미지1", "북마크이미지입니다."),
            ImageItem("https://source.unsplash.com/random/201x200", "이미지2", "북마크이미지입니다."),
            ImageItem("https://source.unsplash.com/random/202x200", "이미지3", "북마크이미지입니다."),
            ImageItem("https://source.unsplash.com/random/203x200", "이미지4", "북마크이미지입니다.")
        )

        binding.rvBookmark.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = BookMarkImageAdapter(itemList)
        }
    }
}