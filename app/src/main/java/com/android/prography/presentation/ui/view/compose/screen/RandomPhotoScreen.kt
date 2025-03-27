package com.android.prography.presentation.ui.view.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.prography.R
import com.android.prography.data.entity.ImageUrls
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.presentation.ui.view.compose.components.SwipeDirection
import com.android.prography.presentation.ui.view.compose.components.SwipeableCard
import com.android.prography.presentation.ui.view.random.RandomPhotoViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun RandomPhotoScreen(
    viewModel: RandomPhotoViewModel = hiltViewModel(),
    onPhotoInfoClick: (String, String, String) -> Unit
) {
    val photos by viewModel.photos.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // 프로그라피 로고
        val prographyLogo = ImageVector.vectorResource(id = R.drawable.ic_prography_logo)
        Image(
            imageVector = prographyLogo,
            contentDescription = "Prography Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        // 구분선
        Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)

        // 카드 스택 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp, bottom = 44.dp)
        ) {
            if (photos.isNotEmpty()) {
                CardStack(
                    photos = photos,
                    onSwipeLeft = { photo ->
                        // 관심 없음 - 좌측 스와이프
                    },
                    onSwipeRight = { photo ->
                        // 북마크 - 우측 스와이프
                        viewModel.bookmarkPhoto(photo)
                    },
                    onInfoClick = { photo ->
                        onPhotoInfoClick(
                            photo.id,
                            photo.imageUrls.small,
                            photo.imageUrls.regular
                        )
                    },
                    onBookmarkClick = { photo ->
                        viewModel.bookmarkPhoto(photo)
                    },
                    onNotInterestedClick = { photo ->
                        // 관심 없음 처리
                    }
                )
            }
        }
    }
}

@Composable
fun CardStack(
    photos: List<PhotoResponse>,
    onSwipeLeft: (PhotoResponse) -> Unit,
    onSwipeRight: (PhotoResponse) -> Unit,
    onInfoClick: (PhotoResponse) -> Unit,
    onBookmarkClick: (PhotoResponse) -> Unit,
    onNotInterestedClick: (PhotoResponse) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        photos.take(5).forEachIndexed { index, photo ->
            Box(
                modifier = Modifier
                    .zIndex(3f - index)
                    .padding(bottom = (index * 12).dp)
            ) {
                SwipeableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    onSwiped = { direction ->
                        when (direction) {
                            SwipeDirection.LEFT -> onSwipeLeft(photo)
                            SwipeDirection.RIGHT -> onSwipeRight(photo)
                            SwipeDirection.NONE -> {}
                        }
                    }
                ) {
                    PhotoCard(
                        photo = photo,
                        onInfoClick = { onInfoClick(photo) },
                        onBookmarkClick = { onBookmarkClick(photo) },
                        onNotInterestedClick = { onNotInterestedClick(photo) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoCard(
    photo: PhotoResponse,
    onInfoClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onNotInterestedClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
    ) {
        // 이미지
        GlideImage(
            model = photo.imageUrls.regular,
            contentDescription = "Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 하단 버튼 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 관심 없음 버튼
            IconButton(
                onClick = onNotInterestedClick,
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_not_interested_button),
                    contentDescription = "Not Interested",
                    tint = Color.White
                )
            }

            // 정보 버튼
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_informationbutton),
                    contentDescription = "Info",
                    tint = Color.White
                )
            }

            // 북마크 버튼
            IconButton(
                onClick = onBookmarkClick,
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = "Bookmark",
                    tint = Color.White
                )
            }
        }
    }
}
