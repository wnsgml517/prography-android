package com.android.prography.presentation.ui.view.compose

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.presentation.ui.base.BaseComposeActivity
import com.android.prography.presentation.ui.view.compose.bottomNav.BottomNavigationBar
import com.android.prography.presentation.ui.view.compose.bottomNav.MainNavigationHost
import com.android.prography.presentation.ui.view.util.GlobalUiHandler
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    private val viewModel: ToDoViewModel by viewModels()

    // 권한 요청 처리기
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한이 승인되면 이미지 피커 실행
            launchScreenshotPicker()
        } else {
            // 권한이 거부되면 사용자에게 알림
            Toast.makeText(this, "스크린샷 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    // 스크린샷 전용 Activity 시작하기 위한 런처
    private val screenshotActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            uri?.let { imageUri ->
                Toast.makeText(this, "스크린샷이 선택되었습니다", Toast.LENGTH_SHORT).show()
                // 필요에 따라 viewModel에 전달
            }
        }
    }

    // 스크린샷만 표시하는 이미지 선택기 실행 (권한 확인 후)
    private fun launchScreenshotPicker() {
        // Android 13(API 33) 이상에서는 READ_MEDIA_IMAGES 권한 확인
        // 이전 버전에서는 READ_EXTERNAL_STORAGE 확인
        val permissionToCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            // 권한이 이미 있는 경우
            ContextCompat.checkSelfPermission(this, permissionToCheck) ==
                    PackageManager.PERMISSION_GRANTED -> {
                // 스크린샷 전용 Activity 실행
                val intent = Intent(this, ScreenshotPickerActivity::class.java)
                screenshotActivityLauncher.launch(intent)
            }
            // 권한 요청이 필요한 경우
            else -> {
                requestPermissionLauncher.launch(permissionToCheck)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            YourAppTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            navController = navController,
                            onItemSelected = { /* 필요시 추가 로직 */ }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // 메인 콘텐츠
                        NavHost(
                            navController = navController,
                            startDestination = "main",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable("main") {
                                MainContent(
                                    onNavigateToScreenshotGallery = {
                                        navController.navigate("screenshot_gallery")
                                    }
                                )
                            }
                            composable("screenshot_gallery") {
                                ScreenshotGalleryScreen(
                                    onNavigateUp = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                            // 기타 필요한 화면 추가
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(
    onNavigateToScreenshotGallery: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 메인 화면 콘텐츠

        // 스크린샷 갤러리 이동 버튼
        Button(
            onClick = onNavigateToScreenshotGallery,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("스크린샷 갤러리")
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