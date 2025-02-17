package com.android.prography.data.entity

import com.squareup.moshi.Json

data class PhotoResponse(
    val id: String,
    @Json(name = "urls") val imageUrls: ImageUrls
)

data class ImageUrls(
    val small: String,  // RecyclerView에 쓸 작은 이미지 URL
    val regular: String // Glide로 로딩할 이미지 URL
)
