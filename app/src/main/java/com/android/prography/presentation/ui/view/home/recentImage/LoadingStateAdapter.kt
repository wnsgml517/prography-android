package com.android.prography.presentation.ui.view.home.recentImage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.prography.R

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_image_loading, parent, false)
        return LoadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        if (loadState is LoadState.Loading) {
            holder.showLoading()
        } else {
            holder.hideLoading()
        }
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val lottieLoader: LottieAnimationView = view.findViewById(R.id.lottieLoader)

        fun showLoading() {
            lottieLoader.visibility = View.VISIBLE
            lottieLoader.playAnimation()
        }

        fun hideLoading() {
            lottieLoader.visibility = View.GONE
            lottieLoader.pauseAnimation()
        }
    }
}
