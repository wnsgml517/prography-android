package com.android.prography.presentation.ui.view.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.databinding.FragmentPhotoDetailBinding
import com.android.prography.presentation.ui.adapter.BookMarkImageAdapter
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.util.HorizontalSpaceItemDecoration
import com.android.prography.presentation.util.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding, PhotoDetailViewModel>(
    FragmentPhotoDetailBinding::inflate,
    PhotoDetailViewModel::class.java
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailImage()
    }

    private fun initDetailImage() {

    }
}