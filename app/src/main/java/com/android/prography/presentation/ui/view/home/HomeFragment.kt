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
        // ✅ Adapter를 먼저 초기화한 후 사용
        recentImageAdapter = RecentImageAdapter()

        binding.rvRecentImage.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = recentImageAdapter
        }

        // ✅ LiveData observer 먼저 설정 후 fetchPhotos() 호출
        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            Timber.i("photo : $photos")
            recentImageAdapter.submitList(photos)
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