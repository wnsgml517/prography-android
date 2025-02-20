package com.android.prography.presentation.ui.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.prography.data.api.BookmarkPhotoDao
import com.android.prography.data.entity.BookmarkPhoto
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.usecase.GetRecentImageUseCase
import com.android.prography.presentation.ui.base.BaseViewModel
import com.android.prography.presentation.ui.ext.parseErrorMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookmarkPhotoDao: BookmarkPhotoDao,
    private val getRecentImageUseCase: GetRecentImageUseCase
): BaseViewModel() {
    private val _photos = MutableLiveData<List<RecentPhotoResponse>>(emptyList())
    val photos: LiveData<List<RecentPhotoResponse>> = _photos

    // 랜덤 사진 읽어올 개수 (5개로 설정)
    private val _countIdx = MutableLiveData(10) // 초기값 5 설정
    val countIdx: LiveData<Int> get() = _countIdx

    private val accessToken = "QncuXcl9I8DrjBvou0gUTPcBwZIz6ZKSTBglJwv6uXY"

    private val _bookmarkedPhotos = MutableStateFlow<List<BookmarkPhoto>>(emptyList())
    val bookmarkedPhotos = _bookmarkedPhotos.asStateFlow()

    init {
        fetchPhotos()
        fetchBookmarks()
    }

    fun bookmarkPhoto(photo: PhotoResponse) {
        viewModelScope.launch {
            bookmarkPhotoDao.insertBookmark(BookmarkPhoto(photo.id, ImageUrls(photo.imageUrls.small,photo.imageUrls.regular)))
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
        viewModelScope.launch(Dispatchers.IO) {
            getRecentImageUseCase(accessToken, 10).onSuccess {
                _photos.postValue(it)
            }.onFailure {
                baseEvent(Event.ShowToast(it.message.parseErrorMsg()))
            }
        }
    }
}