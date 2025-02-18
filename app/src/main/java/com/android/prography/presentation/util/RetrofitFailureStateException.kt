package com.android.prography.presentation.util

class RetrofitFailureStateException(error: String ?, val code: Int) : Exception(error) {
}