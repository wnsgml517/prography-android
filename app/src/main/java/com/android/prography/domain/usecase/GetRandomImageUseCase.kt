package com.android.prography.domain.usecase

import com.android.prography.data.entity.PhotoResponse
import com.android.prography.domain.repository.PhotoRepository
import javax.inject.Inject

class GetRandomImageUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
){
    suspend operator fun invoke(
        accessKey: String,
        countIdx : Int
    ) : Result<List<PhotoResponse>>{
        return photoRepository.getRandomPhotos(accessKey, countIdx)
    }
}