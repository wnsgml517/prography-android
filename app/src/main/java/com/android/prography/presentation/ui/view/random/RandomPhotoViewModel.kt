package com.android.prography.presentation.ui.view.random

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.domain.usecase.GetRandomImageUseCase
import com.android.prography.presentation.ui.base.BaseViewModel
import com.android.prography.presentation.ui.ext.parseErrorMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RandomPhotoViewModel @Inject constructor(
    private val getRandomImageUseCase: GetRandomImageUseCase
): BaseViewModel() {
    private val _photos = MutableLiveData<List<PhotoResponse>>(emptyList())
    val photos: LiveData<List<PhotoResponse>> = _photos

    // 랜덤 사진 읽어올 개수 (5개로 설정)
    private val _countIdx = MutableLiveData(5) // 초기값 5 설정
    val countIdx: LiveData<Int> get() = _countIdx

    private val accessToken = "QncuXcl9I8DrjBvou0gUTPcBwZIz6ZKSTBglJwv6uXY"

    fun fetchPhotos() {
        Timber.i("checking fetchPhotos")
        viewModelScope.launch(Dispatchers.IO) {
            getRandomImageUseCase(accessToken, 5).onSuccess {
                _photos.postValue(it)
            }.onFailure {
                baseEvent(Event.ShowToast(it.message.parseErrorMsg()))
            }
        }
    }
}