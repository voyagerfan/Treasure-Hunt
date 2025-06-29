package com.example.treasurehunt.model

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.treasurehunt.GetGreetingQuery

data class StartGameState(
    val greetings: List<GetGreetingQuery.Greeting?>? = null,
    val currentLocation: Pair<Double, Double>? = null,
    val questRadius: Double? = null,
    val questList: List<QuestItem>? = null
)

data class QuestItem(
    val title: String,
    val description: String,
    val clue: String,
    val hint: String,
    val coordinates: Pair<Double, Double>,
    val rating: Double,
    val endGameAssets: CompletedQuestData
)

data class CompletedQuestData(
    @StringRes val questDetail: Int,
    val questPicture: QuestImage
)

sealed class QuestImage {
    data class Resource(@DrawableRes val resId: Int) : QuestImage()
    data class Url(val imageUrl: String) : QuestImage()
    data class BitmapImage(val bitmap: Bitmap) : QuestImage()
}

