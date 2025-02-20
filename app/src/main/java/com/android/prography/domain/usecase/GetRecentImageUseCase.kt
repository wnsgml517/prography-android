package com.android.prography.domain.usecase

import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.repository.PhotoRepository
import javax.inject.Inject

class GetRecentImageUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
){
    suspend operator fun invoke(
        accessKey: String,
        countIdx : Int,
        page : Int
    ) : Result<List<RecentPhotoResponse>>{
        return photoRepository.getRecentPhotos(accessKey, countIdx, page)
    }
}