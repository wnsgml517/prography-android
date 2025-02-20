package com.android.prography.data.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.prography.data.entity.BookmarkPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(photo: BookmarkPhoto)

    @Query("SELECT * FROM bookmark_photos")
    fun getAllBookmarks(): Flow<List<BookmarkPhoto>>
}
