package com.android.prography.presentation.ui.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.prography.data.api.BookmarkPhotoDao
import com.android.prography.data.entity.BookmarkPhoto
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RandomPhotoResponse
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
class PhotoDetailViewModel @Inject constructor(
    private val bookmarkPhotoDao: BookmarkPhotoDao,
    private val getRecentImageUseCase: GetRecentImageUseCase
): BaseViewModel() {
    private val _photos = MutableLiveData<List<RandomPhotoResponse>>(emptyList())
    val photos: LiveData<List<RandomPhotoResponse>> = _photos

    private val accessToken = ""

    private val _bookmarkedPhotos = MutableStateFlow<List<BookmarkPhoto>>(emptyList())
    val bookmarkedPhotos = _bookmarkedPhotos.asStateFlow()

    init {
        fetchPhotoDetail()
    }

    fun bookmarkPhoto(photo: PhotoResponse) {
        viewModelScope.launch {
            bookmarkPhotoDao.insertBookmark(BookmarkPhoto(photo.id, ImageUrls(photo.imageUrls.small,photo.imageUrls.regular)))
        }
    }

    fun fetchPhotoDetail() {
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