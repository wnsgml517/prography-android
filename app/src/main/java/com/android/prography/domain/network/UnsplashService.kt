package com.android.prography.domain.network

import com.android.prography.data.api.UnsplashApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object UnsplashService {
    private const val BASE_URL = "https://api.unsplash.com/"

    val api: UnsplashApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }
}
