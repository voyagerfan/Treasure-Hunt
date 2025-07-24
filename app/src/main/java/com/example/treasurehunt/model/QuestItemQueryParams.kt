package com.example.treasurehunt.model

data class QuestItemQueryParams(
    val ratingRange: Pair<Float,Float>? = null,
    val radius: Float? = null,
    val originCoordinates: Coordinate? = null
)