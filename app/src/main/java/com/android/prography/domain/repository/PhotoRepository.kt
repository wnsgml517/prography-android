package com.android.prography.domain.repository

import com.android.prography.data.entity.PhotoResponse

interface PhotoRepository{
    suspend fun getRandomPhotos(accessKey: String, countIdx : Int): Result<List<PhotoResponse>>
}