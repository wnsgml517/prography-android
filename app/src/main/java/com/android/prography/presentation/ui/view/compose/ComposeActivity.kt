package com.android.prography.presentation.ui.view.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.presentation.ui.base.BaseComposeActivity
import com.android.prography.presentation.ui.view.compose.bottomNav.BottomNavigationBar
import com.android.prography.presentation.ui.view.compose.bottomNav.MainNavigationHost
import com.android.prography.presentation.ui.view.util.GlobalUiHandler
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass
@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    private val viewModel: ToDoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        navController = navController,
                        onItemSelected = { /* 필요시 추가 로직 */ }
                    )
                }
            ) { innerPadding ->
                MainNavigationHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}


@Composable
fun PhotoDetailScreen(
    smallUrl: String,
    regularUrl: String,
    id: String,
    onBackClick: () -> Unit
) {
    // 포토 상세 화면 구현

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoList(photos: List<PhotoResponse>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // ✅ 아이템 간 간격 설정
    ) {
        items(photos) { photo ->
            GlideImage(
                model = photo.imageUrls.small,
                contentDescription = "Loaded Image",
                modifier = Modifier
                    .height(150.dp) // ✅ 높이만 고정
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun YourAppTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
            GlobalUiHandler()
        }
    }
}

data class ToDoData(
    val key: Int, val text: String, val done: Boolean = false
)