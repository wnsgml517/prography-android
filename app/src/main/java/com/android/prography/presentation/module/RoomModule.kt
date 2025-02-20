package com.android.prography.presentation.module

import android.content.Context
import androidx.room.Room
import com.android.prography.data.api.BookmarkPhotoDao
import com.android.prography.data.repository.remote.photo.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookmarkPhotoDao(database: AppDatabase): BookmarkPhotoDao {
        return database.bookmarkPhotoDao()
    }
}
