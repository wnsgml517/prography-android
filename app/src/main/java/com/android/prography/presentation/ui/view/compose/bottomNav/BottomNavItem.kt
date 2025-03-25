package com.android.prography.presentation.ui.view.compose.bottomNav

import com.android.prography.R

// 네비게이션 아이템 정의
sealed class BottomNavItem(
    val route: String, 
    val title: Int, 
    val icon: Int
) {
    object Home : BottomNavItem("home", R.string.title_home, R.drawable.ic_tapbar_main)
    object Random : BottomNavItem("random", R.string.title_random, R.drawable.ic_tapbar_cards)
}