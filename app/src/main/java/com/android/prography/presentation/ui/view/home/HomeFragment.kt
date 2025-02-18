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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecentImage()
        initBookmarkImage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecentImage() {
        val itemList = listOf(
            ImageItem(
                "https://source.unsplash.com/random/300x200",
                "titletitletitle",
                "타이틀은 최대 2줄까지"
            ),
            ImageItem(
                "https://source.unsplash.com/random/301x350",
                "titletitletitle",
                "타이틀은 최대 2줄까지"
            ),
            ImageItem(
                "https://source.unsplash.com/random/302x250",
                "titletitletitle",
                "타이틀은 최대 2줄까지"
            ),
            ImageItem(
                "https://source.unsplash.com/random/303x400",
                "titletitletitle",
                "타이틀은 최대 2줄까지"
            )
        )

        binding.rvRecentImage.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvRecentImage.adapter = RecentImageAdapter(itemList)
    }


    private fun initBookmarkImage() {
        val itemList = listOf(
            ImageItem("https://source.unsplash.com/random/200x200", "이미지1","북마크이미지입니다."),
            ImageItem("https://source.unsplash.com/random/201x200", "이미지2","북마크이미지입니다."),
            ImageItem("https://source.unsplash.com/random/202x200", "이미지3","북마크이미지입니다."),
            ImageItem("https://source.unsplash.com/random/203x200", "이미지4","북마크이미지입니다.")
        )

        binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvBookmark.adapter = BookMarkImageAdapter(itemList)
    }
}