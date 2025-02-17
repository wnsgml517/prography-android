package com.android.prography.data.api
import com.android.prography.data.entity.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String
    ): List<PhotoResponse>
}
