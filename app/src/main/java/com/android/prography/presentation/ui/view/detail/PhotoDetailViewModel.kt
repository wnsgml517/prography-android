package com.android.prography.presentation.ui.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.prography.data.api.BookmarkPhotoDao
import com.android.prography.data.entity.BookmarkPhoto
import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.usecase.GetDetailImageUseCase
import com.android.prography.domain.usecase.GetRandomImageUseCase
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
    stateHandle: SavedStateHandle,
    private val bookmarkPhotoDao: BookmarkPhotoDao,
    private val getDetailImageUseCase: GetDetailImageUseCase
): BaseViewModel() {

    var id: LiveData<String> = stateHandle.getLiveData("id")
    var smallUrl: LiveData<String> = stateHandle.getLiveData("smallUrl")
    var regularUrl: LiveData<String> = stateHandle.getLiveData("regularUrl")

    private val _detailPhoto = MutableLiveData<DetailPhotoResponse>()
    val detailPhoto: LiveData<DetailPhotoResponse> = _detailPhoto


    private val _bookmarkedPhotos = MutableStateFlow<List<BookmarkPhoto>>(emptyList())
    val bookmarkedPhotos = _bookmarkedPhotos.asStateFlow()

    private val _isBookMark = MutableLiveData<Boolean>()
    val isBookMark: LiveData<Boolean> = _isBookMark

    private val accessToken = "QncuXcl9I8DrjBvou0gUTPcBwZIz6ZKSTBglJwv6uXY"

    init {
        fetchPhotoDetail()
        fetchBookmarks()
    }

    fun bookmarkPhoto(photo: PhotoResponse) {
        viewModelScope.launch {
            bookmarkPhotoDao.insertBookmark(BookmarkPhoto(photo.id, ImageUrls(photo.imageUrls.small,photo.imageUrls.regular)))
        }
    }

    fun fetchBookmarks() {
        viewModelScope.launch {
            val isBookmarked = bookmarkPhotoDao.isPhotoBookmarked(id.value!!) > 0
            _isBookMark.postValue(isBookmarked)
        }
    }

    fun fetchPhotoDetail() {
        Timber.i("checking fetchPhotoDetail")

        viewModelScope.launch(Dispatchers.IO) {
            getDetailImageUseCase(accessToken, id.value ?: "").onSuccess {
                _detailPhoto.postValue(it)
            }.onFailure {
                baseEvent(Event.ShowToast(it.message.parseErrorMsg()))
            }
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {

            // Room DB 데이터 업데이트
            if (isBookMark.value!!) {
                removeBookmark(id.value!!)
            } else {
                addBookmark()
            }

            // 북마크 상태 변경
            _isBookMark.postValue(!isBookMark.value!!)
        }
    }
    // ✅ 북마크 추가
    private fun addBookmark() {
        viewModelScope.launch {
            val bookmark = BookmarkPhoto(id = id.value!! , imageUrl = ImageUrls(smallUrl.value!!, regularUrl.value!!))
            bookmarkPhotoDao.insertBookmark(bookmark)
        }
    }

    // ✅ 북마크 삭제
    private fun removeBookmark(photoId: String) {
        viewModelScope.launch {
            bookmarkPhotoDao.deleteBookmark(photoId)
        }
    }
}