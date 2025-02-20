package com.android.prography.presentation.ui.view.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.databinding.FragmentPhotoDetailBinding
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.util.PhotoUtil
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding, PhotoDetailViewModel>(
    FragmentPhotoDetailBinding::inflate,
    PhotoDetailViewModel::class.java
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailImage()
        initDetailInform()
        initSetOnclickListener()
    }

    private fun initDetailImage() {
        // 파라미터로 가져온 이미지
        Glide.with(binding.root.context)
            .load(viewModel.regularUrl.value)
            .into(binding.ivDetailImage)
    }

    private fun initDetailInform() {
        // 정보 셋팅
        viewModel.detailPhoto.observe(viewLifecycleOwner) { photos ->
            settingUI(photos)
            // local DB 에 저장되어 있는 북마크 id와 일치하는 지 확인하는 코드 작성
            // 북마크 이미지라면 binding.ivBookMark 우측 상단 북마크 버튼 : 북마크 되어있지 않다면 opacity 100%, 북마크 되었다면 opacity 30%
        }

        // 포토 디테일 북마크 여부 가져오기
        viewModel.isBookMark.observe(viewLifecycleOwner) {
            updateBookmarkUI(it)
        }
    }

    private fun settingUI(photos: DetailPhotoResponse) {
        binding.tvUserName.text = photos.user.username ?: "Not Found User"
        binding.tvDescription.text = photos.description ?: "No Description"
        binding.tvTagList.text = photos.tags.joinToString { tag -> "#${tag.title} " }
    }

    private fun initSetOnclickListener() = with(binding) {
        btnCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }
        btnDownloadImage.setOnClickListener {
            // 현재 보여지는 이미지 다운로드
            PhotoUtil.downloadImage(requireContext(), viewModel.regularUrl.value)
        }
        btnBookmarkButton.setOnClickListener {
            // 현재 보여지는 이미지가 북마크인 상태에서 버튼을 누르면 북마크 해제
            // 현재 보여지는 이미지가 북마크가 아닌 상태라면, 버튼을 누르면 북마크 추가
            viewModel.toggleBookmark()
        }
    }

    // ✅ 북마크 상태에 따라 UI 업데이트
    private fun updateBookmarkUI(isBookmarked: Boolean) {
        binding.btnBookmarkButton.alpha = if (isBookmarked) 1.0f else 0.3f
    }
}