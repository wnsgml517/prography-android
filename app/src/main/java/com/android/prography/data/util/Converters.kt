package com.android.prography.data.util

import androidx.room.TypeConverter
import com.android.prography.data.entity.ImageUrls
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromImageUrls(imageUrls: ImageUrls): String {
        return gson.toJson(imageUrls)
    }

    @TypeConverter
    fun toImageUrls(imageUrlsString: String): ImageUrls {
        val type = object : TypeToken<ImageUrls>() {}.type
        return gson.fromJson(imageUrlsString, type)
    }
}
