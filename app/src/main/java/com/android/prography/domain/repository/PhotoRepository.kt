package com.android.prography.domain.repository

import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RandomPhotoResponse

interface PhotoRepository{
    suspend fun getRandomPhotos(accessKey: String, countIdx : Int): Result<List<PhotoResponse>>
    suspend fun getRecentPhotos(accessKey: String, countIdx : Int): Result<List<RandomPhotoResponse>>
}