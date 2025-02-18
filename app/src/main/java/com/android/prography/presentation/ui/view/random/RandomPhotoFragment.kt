package com.android.prography.presentation.ui.view.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.prography.R
import com.android.prography.databinding.FragmentRandomPhotoBinding
import com.android.prography.presentation.ui.base.BaseFragment
import com.android.prography.presentation.ui.view.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class RandomPhotoFragment : BaseFragment<FragmentRandomPhotoBinding, RandomPhotoViewModel>(
    FragmentRandomPhotoBinding::inflate,
    RandomPhotoViewModel::class.java
) {

    private lateinit var adapter: RandomPhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RandomPhotoAdapter()

        binding.rvRandomView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@RandomPhotoFragment.adapter
        }

        val itemTouchHelper = ItemTouchHelper(SwipeToDismissCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvRandomView)

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            Timber.i("photo : ${photos}")
            adapter.submitList(photos)
        }

        viewModel.fetchPhotos()
    }
}