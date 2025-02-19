package com.android.prography.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomPhotoResponse(
    val id: String,
    @SerialName("alt_description") val title: String?, // ✅ Title 역할
    @SerialName("urls") val imageUrls: Urls
)

@Serializable
data class Urls(
    val small: String,
    val regular: String
)
