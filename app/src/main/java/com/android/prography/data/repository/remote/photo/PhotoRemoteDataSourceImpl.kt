package com.android.prography.data.repository.remote.photo

import com.android.prography.data.api.PhotoService
import com.android.prography.data.entity.PhotoResponse
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
}