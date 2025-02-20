package com.android.prography.domain.usecase

import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.repository.PhotoRepository
import javax.inject.Inject

class GetDetailImageUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
){
    suspend operator fun invoke(
        accessKey: String,
        photoId: String
    ) : Result<DetailPhotoResponse>{
        return photoRepository.getDetailPhoto(accessKey, photoId)
    }
}