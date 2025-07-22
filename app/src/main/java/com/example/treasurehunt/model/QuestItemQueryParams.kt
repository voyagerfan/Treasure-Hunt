package com.example.treasurehunt.model

import androidx.annotation.FloatRange

data class QuestItemQueryParams(
    val ratingRange: Pair<Float,Float>? = null,
    val radius: Float? = null,
    val originCoordinates: Coordinate? = null
)