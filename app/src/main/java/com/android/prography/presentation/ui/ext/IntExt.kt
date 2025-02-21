package com.android.prography.presentation.ui.ext

import android.content.res.Resources

fun Int.DpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()