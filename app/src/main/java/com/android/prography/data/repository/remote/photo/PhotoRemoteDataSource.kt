package com.android.prography.data.repository.remote.photo

import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.util.NetworkState

interface PhotoRemoteDataSource {
    suspend fun getRandomPhotos(accessKey: String, countIdx : Int): NetworkState<List<PhotoResponse>>
    suspend fun getRecentPhotos(accessKey: String, countIdx : Int, page : Int): NetworkState<List<RecentPhotoResponse>>
    suspend fun getDetailPhoto(accessKey: String, photoId : String): NetworkState<DetailPhotoResponse>
}