package com.example.treasurehunt.model

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

sealed class QuestImage {
    data class Resource(@DrawableRes val resId: Int) : QuestImage()
    data class Url(val imageUrl: String) : QuestImage()
    data class BitmapImage(val bitmap: Bitmap) : QuestImage()
}