package com.android.prography.presentation.ui.view.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.prography.presentation.ui.view.home.HomeViewModel
import com.android.prography.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPhotoClick: (String, String, String) -> Unit
) {
    val bookmarkedPhotos by viewModel.bookmarkedPhotos.collectAsState(initial = emptyList())
    val recentPhotos = viewModel.recentPhotosFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        // 프로그라피 로고
        Image(
            painter = painterResource(id = R.drawable.ic_prography_logo),
            contentDescription = "Prography Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        // 구분선
        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.3f))

        // 북마크 섹션
        if (bookmarkedPhotos.isNotEmpty()) {
            Text(
                text = "북마크",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )

            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bookmarkedPhotos) { bookmark ->
                    BookmarkPhotoItem(
                        imageUrl = bookmark.imageUrl.small,
                        onClick = { 
                            onPhotoClick(
                                bookmark.id, 
                                bookmark.imageUrl.small, 
                                bookmark.imageUrl.regular
                            ) 
                        }
                    )
                }
            }
        }

        // 최신 이미지 섹션
        Text(
            text = "최신 이미지",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .padding(top = 12.dp)
        )

        // 최신 이미지 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(recentPhotos.itemCount) { index ->
                val photo = recentPhotos[index]
                photo?.let {
                    RecentPhotoItem(
                        imageUrl = it.imageUrls.small,
                        onClick = { 
                            onPhotoClick(
                                it.id, 
                                it.imageUrls.small, 
                                it.imageUrls.regular
                            ) 
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookmarkPhotoItem(
    imageUrl: String,
    onClick: () -> Unit
) {
    GlideImage(
        model = imageUrl,
        contentDescription = "Bookmark Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(120.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecentPhotoItem(
    imageUrl: String,
    onClick: () -> Unit
) {
    GlideImage(
        model = imageUrl,
        contentDescription = "Recent Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    )
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            // 실제 프리뷰에 사용할 모의 데이터나 로직 추가
            HomeScreen(
                // ViewModel 대신 사용할 모의 데이터 제공
                // 예를 들어, viewModel = previewViewModel,
                onPhotoClick = { id, smallUrl, regularUrl ->
                    // 프리뷰에서 클릭 이벤트 처리 (선택사항)

                }
            )
        }
    }
}
