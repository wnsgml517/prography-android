package com.android.prography.domain.usecase

import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RandomPhotoResponse
import com.android.prography.domain.repository.PhotoRepository
import javax.inject.Inject

class GetRecentImageUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
){
    suspend operator fun invoke(
        accessKey: String,
        countIdx : Int
    ) : Result<List<RandomPhotoResponse>>{
        return photoRepository.getRecentPhotos(accessKey, countIdx)
    }
}