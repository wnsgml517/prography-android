package com.android.prography.presentation.ui.view.compose.bottomNav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.prography.presentation.ui.view.compose.PhotoDetailScreen
import com.android.prography.presentation.ui.view.compose.screen.HomeScreen
import com.android.prography.presentation.ui.view.compose.screen.RandomPhotoScreen

@Composable
fun MainNavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, 
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        // 홈 화면
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                onPhotoClick = { smallUrl, regularUrl, id ->
                    navController.navigate("detail/$smallUrl/$regularUrl/$id")
                }
            )
        }

        // 랜덤 화면
        composable(BottomNavItem.Random.route) {
            RandomPhotoScreen(
                onPhotoInfoClick = { smallUrl, regularUrl, id ->
                    navController.navigate("detail/$smallUrl/$regularUrl/$id")
                }
            )
        }

        // 상세 화면 (딥링크 형식으로 파라미터 전달)
        composable(
            route = "detail/{smallUrl}/{regularUrl}/{id}",
            arguments = listOf(
                navArgument("smallUrl") { type = NavType.StringType },
                navArgument("regularUrl") { type = NavType.StringType },
                navArgument("id") { 
                    type = NavType.StringType
                    defaultValue = "" 
                }
            )
        ) { backStackEntry ->
            val smallUrl = backStackEntry.arguments?.getString("smallUrl") ?: ""
            val regularUrl = backStackEntry.arguments?.getString("regularUrl") ?: ""
            val id = backStackEntry.arguments?.getString("id") ?: ""
            
            PhotoDetailScreen(
                smallUrl = smallUrl,
                regularUrl = regularUrl,
                id = id,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}