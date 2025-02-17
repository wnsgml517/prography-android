package com.android.prography.ui.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.prography.R
import com.android.prography.databinding.FragmentRandomPhotoBinding
import kotlinx.coroutines.flow.collectLatest

class RandomPhotoFragment : Fragment() {

    private var _binding: FragmentRandomPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RandomPhotoViewModel by viewModels() // ViewModel 설정


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }
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

        lifecycleScope.launchWhenStarted {
            viewModel.photos.collectLatest { photos ->
                adapter.submitList(photos)
            }
        }

        viewModel.fetchPhotos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}