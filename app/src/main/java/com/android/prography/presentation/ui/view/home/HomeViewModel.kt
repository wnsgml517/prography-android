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

    // 랜덤 이미지 응답 값
    private val _photos = MutableLiveData<List<RecentPhotoResponse>>(emptyList())
    val photos: LiveData<List<RecentPhotoResponse>> = _photos

    // 랜덤 사진 읽어올 개수 (5개로 설정)
    private val _countIdx = MutableLiveData(10) // 초기값 5 설정
    val countIdx: LiveData<Int> get() = _countIdx

    private val _currentPage = MutableLiveData(1) // ✅ 현재 페이지를 저장

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val accessToken = "QncuXcl9I8DrjBvou0gUTPcBwZIz6ZKSTBglJwv6uXY"

    private val _bookmarkedPhotos = MutableStateFlow<List<BookmarkPhoto>>(emptyList())
    val bookmarkedPhotos = _bookmarkedPhotos.asStateFlow()

    init {
        fetchPhotos()
        fetchBookmarks()
    }

    fun fetchBookmarks() {
        viewModelScope.launch {
            bookmarkPhotoDao.getAllBookmarks().collect { bookmarks ->
                _bookmarkedPhotos.value = bookmarks
            }
        }
    }

    fun fetchPhotos() {
        if (_isLoading.value == true) return // ✅ 이미 로딩 중이면 추가 요청 방지
        _isLoading.value = true

        Timber.i("fetchPhotos 호출: 페이지 $_currentPage")

        viewModelScope.launch(Dispatchers.IO) {
            getRecentImageUseCase(accessToken, 10, _currentPage.value ?: 1).onSuccess { newPhotos ->
                val updatedList = _photos.value.orEmpty() + newPhotos // ✅ 기존 데이터에 새 데이터 추가
                _photos.postValue(updatedList)
                _currentPage.postValue((_currentPage.value ?: 1) + 1) // ✅ 페이지 증가
                _isLoading.postValue(false)
            }.onFailure {
                baseEvent(Event.ShowToast(it.message.parseErrorMsg()))
                _isLoading.postValue(false)
            }
        }
    }
}