package com.android.prography.data.api
import com.android.prography.data.entity.DetailPhotoResponse
import com.android.prography.data.entity.PhotoResponse
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.util.NetworkState
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoService {
    @GET("photos/random/")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String,
        @Query("count") countIdx : Int
    ): NetworkState<List<PhotoResponse>>

    @GET("photos")
    suspend fun getRecentPhotos(
        @Query("client_id") clientId: String,
        @Query("count") countIdx : Int
    ): NetworkState<List<RecentPhotoResponse>>

    @GET("photos/{id}")
    suspend fun getDetailPhoto(
        @Path("id") photoId : String,
        @Query("client_id") clientId: String
    ): NetworkState<DetailPhotoResponse>
}
