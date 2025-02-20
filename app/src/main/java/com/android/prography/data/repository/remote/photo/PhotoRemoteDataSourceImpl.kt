package com.android.prography.data.repository.remote.photo

import com.android.prography.data.api.PhotoService
import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.util.NetworkState
import javax.inject.Inject

class PhotoRemoteDataSourceImpl @Inject constructor(
    private val photoService: PhotoService
) : PhotoRemoteDataSource {
    override suspend fun getRandomPhotos(
        accessKey : String,
        count : Int
    ): NetworkState<List<PhotoResponse>> {
        return photoService.getRandomPhotos(accessKey, count)
    }

    override suspend fun getRecentPhotos(
        accessKey : String,
        count : Int,
        page : Int
    ): NetworkState<List<RecentPhotoResponse>> {
        return photoService.getRecentPhotos(accessKey, count, page)
    }

    override suspend fun getDetailPhoto(
        accessKey: String,
        photoId: String
    ): NetworkState<DetailPhotoResponse> {
        return photoService.getDetailPhoto(photoId, accessKey)
    }
}