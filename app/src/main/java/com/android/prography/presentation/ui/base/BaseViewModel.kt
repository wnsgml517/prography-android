package com.android.prography.presentation.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.prography.presentation.util.MutableEventFlow
import com.android.prography.presentation.util.asEventFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _baseEventFlow = MutableEventFlow<Event>()
    val baseEventFlow = _baseEventFlow.asEventFlow()
    fun baseEvent(event: Event) {
        viewModelScope.launch {
            _baseEventFlow.emit(event)
        }
    }
    sealed class Event {
        data class ShowToast(val message: String) : Event()
        data class ShowToastRes(@StringRes val message: Int) : Event()
        data class ShowSuccessToast(val message: String) : Event()
        data class ShowSuccessToastRes(@StringRes val message: Int) : Event()
        
        object ShowLoading: Event()
        object HideLoading: Event()
        object ExpiredToken: Event()
    }
}