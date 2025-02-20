package com.android.prography.domain.repository

import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse

interface PhotoRepository{
    suspend fun getRandomPhotos(accessKey: String, countIdx : Int): Result<List<PhotoResponse>>
    suspend fun getRecentPhotos(accessKey: String, countIdx : Int, page : Int): Result<List<RecentPhotoResponse>>
    suspend fun getDetailPhoto(accessKey: String, photoId : String): Result<DetailPhotoResponse>
}