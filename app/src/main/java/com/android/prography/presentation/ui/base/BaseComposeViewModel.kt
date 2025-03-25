package com.android.prography.presentation.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.prography.presentation.ui.view.util.GlobalUiManager
import com.android.prography.presentation.ui.view.util.UiEvent
import com.android.prography.presentation.util.MutableEventFlow
import com.android.prography.presentation.util.asEventFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Base ViewModel 수정
abstract class BaseComposeViewModel : ViewModel() {
    fun showLoading() {
        GlobalUiManager.sendEvent(UiEvent.ShowLoading)
    }

    fun hideLoading() {
        GlobalUiManager.sendEvent(UiEvent.HideLoading)
    }

    fun showToast(message: String) {
        GlobalUiManager.sendEvent(UiEvent.ShowToast(message))
    }
}