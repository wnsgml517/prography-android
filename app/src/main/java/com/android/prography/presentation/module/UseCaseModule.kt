package com.android.prography.presentation.module

import com.android.prography.domain.repository.PhotoRepository
import com.android.prography.domain.usecase.GetRandomImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetRandomImageUseCase(photoRepository: PhotoRepository) = GetRandomImageUseCase(photoRepository)

}