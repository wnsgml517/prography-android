package com.android.prography.data.repository.remote.photo

import com.android.prography.data.entity.PhotoResponse
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

}
