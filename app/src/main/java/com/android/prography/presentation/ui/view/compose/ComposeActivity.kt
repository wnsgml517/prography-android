package com.android.prography.presentation.ui.view.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.presentation.ui.base.BaseComposeActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class ComposeActivity : BaseComposeActivity<ToDoViewModel>() {

    override fun getViewModelClass(): KClass<ToDoViewModel> = ToDoViewModel::class

    @Composable
    override fun ProvideUI(viewModel: ToDoViewModel) {
        // 최신 이미지 값 가져옴.
        val photos by viewModel.photos.collectAsState()
        PhotoList(photos = photos)
    }
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

data class ToDoData(
    val key: Int, val text: String, val done: Boolean = false
)