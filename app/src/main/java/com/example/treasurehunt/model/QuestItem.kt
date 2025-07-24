package com.example.treasurehunt.model

data class QuestItem(
    val title: String,
    val description: String,
    val clue: String,
    val hint: String,
    val coordinates: Coordinate,
    val rating: Float,
    val endGameAssets: CompletedQuestData
)
