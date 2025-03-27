package com.android.prography.presentation.ui.view.random

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.prography.data.api.BookmarkPhotoDao
import com.android.prography.data.entity.BookmarkPhoto
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.domain.usecase.GetRandomImageUseCase
import com.android.prography.presentation.ui.base.BaseViewModel
import com.android.prography.presentation.ui.ext.parseErrorMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

import com.android.prography.BuildConfig.API_KEY
import com.android.prography.presentation.ui.base.BaseComposeViewModel
import kotlinx.coroutines.delay

@HiltViewModel
class RandomPhotoViewModel @Inject constructor(
    private val bookmarkPhotoDao: BookmarkPhotoDao,
    private val getRandomImageUseCase: GetRandomImageUseCase
): BaseComposeViewModel() {
    private val _photos = MutableStateFlow<List<PhotoResponse>>(emptyList())
    val photos: StateFlow<List<PhotoResponse>> = _photos

    // 랜덤 사진 읽어올 개수 (5개로 설정)
    private val _countIdx = MutableLiveData(5) // 초기값 5 설정
    val countIdx: LiveData<Int> get() = _countIdx


    private val _bookmarkedPhotos = MutableStateFlow<List<BookmarkPhoto>>(emptyList())
    val bookmarkedPhotos = _bookmarkedPhotos.asStateFlow()

    init {
        fetchPhotos()
    }

    fun bookmarkPhoto(photo: PhotoResponse) {
        viewModelScope.launch {
            bookmarkPhotoDao.insertBookmark(BookmarkPhoto(photo.id, ImageUrls(photo.imageUrls.small, photo.imageUrls.regular)))
        }
    }

    fun fetchBookmarks() {
        viewModelScope.launch {
            bookmarkPhotoDao.getAllBookmarks().collect { bookmarks ->
                _bookmarkedPhotos.value = bookmarks
            }
        }
    }

    fun fetchPhotos() {
        Timber.i("checking fetchPhotos")
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            getRandomImageUseCase(API_KEY, 5).onSuccess {
                hideLoading()
                showToast("성공!")
                _photos.emit(it)

            }.onFailure {
                hideLoading()
                showToast(it.message.parseErrorMsg())
            }
        }
    }
}