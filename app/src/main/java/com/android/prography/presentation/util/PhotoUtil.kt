package com.android.prography.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object PhotoUtil {

    // ✅ 이미지 다운로드 시작 (권한 체크 없이 실행)
    fun downloadImage(context: Context, imageUrl: String?) {
        if (imageUrl.isNullOrEmpty()) return

        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveImageToGallery(context, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    // ✅ 비트맵을 갤러리에 저장 (권한 없이 가능)
    private fun saveImageToGallery(context: Context, bitmap: Bitmap) {
        val filename = "photo_${System.currentTimeMillis()}.jpg"
        val fos: OutputStream?

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = uri?.let { context.contentResolver.openOutputStream(it) }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
