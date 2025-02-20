package com.android.prography.data.repository.remote.photo

import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.repository.PhotoRepository
import com.android.prography.domain.util.NetworkState
import com.android.prography.presentation.util.RetrofitFailureStateException
import timber.log.Timber
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoRemoteDataSourceImpl: PhotoRemoteDataSourceImpl) :
    PhotoRepository {
    override suspend fun getRandomPhotos(accessKey: String, countIdx : Int): Result<List<PhotoResponse>> {
        when (val data = photoRemoteDataSourceImpl.getRandomPhotos(accessKey, countIdx)) {
            is NetworkState.Success -> return Result.success(data.body)
            is NetworkState.Failure -> return Result.failure(
                RetrofitFailureStateException(data.error, data.code)
            )

            is NetworkState.NetworkError -> return Result.failure(IllegalStateException("NetworkError"))
            is NetworkState.UnknownError -> {
                Timber.e(data.t?.message)
                return Result.failure(IllegalStateException("unKnownError"))
            }
        }
    }
    override suspend fun getRecentPhotos(accessKey: String, countIdx : Int, page : Int): Result<List<RecentPhotoResponse>> {
        when (val data = photoRemoteDataSourceImpl.getRecentPhotos(accessKey, countIdx, page)) {
            is NetworkState.Success -> return Result.success(data.body)
            is NetworkState.Failure -> return Result.failure(
                RetrofitFailureStateException(data.error, data.code)
            )

            is NetworkState.NetworkError -> return Result.failure(IllegalStateException("NetworkError"))
            is NetworkState.UnknownError -> {
                Timber.e(data.t?.message)
                return Result.failure(IllegalStateException("unKnownError"))
            }
        }
    }
    override suspend fun getDetailPhoto(accessKey: String, photoId : String): Result<DetailPhotoResponse> {
        when (val data = photoRemoteDataSourceImpl.getDetailPhoto(accessKey, photoId)) {
            is NetworkState.Success -> return Result.success(data.body)
            is NetworkState.Failure -> return Result.failure(
                RetrofitFailureStateException(data.error, data.code)
            )

            is NetworkState.NetworkError -> return Result.failure(IllegalStateException("NetworkError"))
            is NetworkState.UnknownError -> {
                Timber.e(data.t?.message)
                return Result.failure(IllegalStateException("unKnownError"))
            }
        }
    }
}
