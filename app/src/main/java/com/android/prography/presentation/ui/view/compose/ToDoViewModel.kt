package com.android.prography.presentation.ui.view.compose

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.prography.BuildConfig.API_KEY
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.domain.usecase.GetRandomImageUseCase
import com.android.prography.domain.usecase.GetRecentImageUseCase
import com.android.prography.presentation.ui.base.BaseViewModel
import com.android.prography.presentation.ui.ext.parseErrorMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val getRandomImageUseCase: GetRandomImageUseCase
) : BaseViewModel() {
    var text = mutableStateOf("")
    val toDoList = mutableStateListOf<ToDoData>()
    private var key = mutableIntStateOf(-1)

    val onSubmit: (String) -> Unit = { text ->
        val exists = toDoList.find { it.key == key.intValue + 1 } != null

        if (!exists) {
            key.intValue += 1
        } else {
            var keyValue = key.intValue
            for (i in key.intValue until 100) {
                if (toDoList.find { it.key == keyValue + 1 } == null) {
                    key.intValue = keyValue + 1
                    break
                } else {
                    keyValue++
                }
            }
        }
        toDoList.add(
            ToDoData(
                key = key.value, text = text
            )
        )
        this.text.value = ""
    }
    val onToggle: (Int, Boolean) -> Unit = { key, checked ->
        val pos = toDoList.indexOfFirst {
            it.key == key
        }
        toDoList[pos] = toDoList[pos].copy(done = checked)
    }
    val onDelete: (Int) -> Unit = { key ->
        this.key.intValue -= 1
        val pos = toDoList.indexOfFirst {
            it.key == key
        }
        toDoList.removeAt(pos)
    }

    val onEdit: (Int, String) -> Unit = { key, text ->
        val pos = toDoList.indexOfFirst {
            it.key == key
        }
        toDoList[pos] = toDoList[pos].copy(text = text)
    }

    private val _photos = MutableStateFlow<List<PhotoResponse>>(emptyList())
    val photos: StateFlow<List<PhotoResponse>> = _photos

    init {
        fetchPhotos()
    }

    fun fetchPhotos() {
        Timber.i("checking fetchPhotos")
        baseEvent(Event.ShowLoading) // ✅ 로딩 시작
        viewModelScope.launch(Dispatchers.IO) {
            getRandomImageUseCase(API_KEY, 5).onSuccess {
                _photos.value = it
                delay(1000)
                baseEvent(Event.HideLoading)
                baseEvent(Event.ShowToast("성공입니다!!"))
            }.onFailure {
                baseEvent(Event.ShowToast(it.message.parseErrorMsg()))
            }
        }
    }
}
