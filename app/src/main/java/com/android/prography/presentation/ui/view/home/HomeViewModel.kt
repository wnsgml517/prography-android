package com.android.prography.presentation.ui.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RandomPhotoResponse
import com.android.prography.domain.usecase.GetRandomImageUseCase
import com.android.prography.domain.usecase.GetRecentImageUseCase
import com.android.prography.presentation.ui.base.BaseViewModel
import com.android.prography.presentation.ui.ext.parseErrorMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentImageUseCase: GetRecentImageUseCase
): BaseViewModel() {
    private val _photos = MutableLiveData<List<RandomPhotoResponse>>(emptyList())
    val photos: LiveData<List<RandomPhotoResponse>> = _photos

    // 랜덤 사진 읽어올 개수 (5개로 설정)
    private val _countIdx = MutableLiveData(5) // 초기값 5 설정
    val countIdx: LiveData<Int> get() = _countIdx

    private val accessToken = "QncuXcl9I8DrjBvou0gUTPcBwZIz6ZKSTBglJwv6uXY"

    init {
        fetchPhotos()
    }

    fun fetchPhotos() {
        Timber.i("checking fetchPhotos")
        viewModelScope.launch(Dispatchers.IO) {
            getRecentImageUseCase(accessToken, 5).onSuccess {
                _photos.postValue(it)
            }.onFailure {
                baseEvent(Event.ShowToast(it.message.parseErrorMsg()))
            }
        }
    }
}