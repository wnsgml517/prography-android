package com.android.prography.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailPhotoResponse(
    val id: String,
    val description: String?,
    val tags: List<Tag>,
    @SerialName("user") val user: User
)

@Serializable
data class User(
    val username: String?
)

@Serializable
data class Tag(
    val title: String
)