package com.android.prography.presentation.ui.view.util

// UI 이벤트 정의 (이전과 동일)
sealed class UiEvent {
    object ShowLoading : UiEvent()
    object HideLoading : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}