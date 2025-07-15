package com.example.treasurehunt.data

import android.graphics.Bitmap
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("/images")
    suspend fun fetchImage(
        @Query("imageName") imageName: Bitmap,
    )
}
