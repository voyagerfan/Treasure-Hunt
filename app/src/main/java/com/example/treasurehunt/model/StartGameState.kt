package com.example.treasurehunt.model

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
    val rating: Double
)

