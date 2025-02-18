package com.android.prography.presentation.module

import android.content.Context
import com.android.prography.data.util.CustomCallAdapterFactory
import com.android.prography.data.util.SharedPreferenceUtil
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideSharedPreferenceUtil(@ApplicationContext context: Context): SharedPreferenceUtil = SharedPreferenceUtil(context)


    @Singleton
    @Provides
    fun provideOkHttpClient() = run {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = run {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.unsplash.com/")
            .addCallAdapterFactory(CustomCallAdapterFactory())
            .addConverterFactory(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true // 지정되지 않은 key 값은 무시
                    coerceInputValues = true // default 값 설정
                    explicitNulls = false // 없는 필드는 null로 설정
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }
}