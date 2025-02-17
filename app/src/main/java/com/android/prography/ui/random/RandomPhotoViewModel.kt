package com.android.prography.ui.random

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.domain.network.UnsplashService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RandomPhotoViewModel : ViewModel() {
    private val _photos = MutableStateFlow<List<PhotoResponse>>(emptyList())
    val photos: StateFlow<List<PhotoResponse>> = _photos

    private val accessToken = "YOUR_ACCESS_TOKEN"

    fun fetchPhotos() {
        viewModelScope.launch {
            try {
                val response = UnsplashService.api.getRandomPhotos(accessToken)
                _photos.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}