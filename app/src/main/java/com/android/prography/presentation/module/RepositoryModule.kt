package com.android.prography.presentation.module

import com.android.prography.data.repository.remote.photo.PhotoRemoteDataSourceImpl
import com.android.prography.data.repository.remote.photo.PhotoRepositoryImpl
import com.android.prography.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePhotoRepository(
        photoRemoteDataSourceImpl: PhotoRemoteDataSourceImpl
    ) : PhotoRepository {
        return PhotoRepositoryImpl(
            photoRemoteDataSourceImpl
        )
    }
}