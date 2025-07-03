package com.example.treasurehunt.ui.state

import com.example.treasurehunt.GetGreetingQuery
import com.example.treasurehunt.model.Coordinate
import com.example.treasurehunt.model.QuestItem

data class StartGameState(
    val greetings: List<GetGreetingQuery.Greeting?>? = emptyList(),
    val currentLocation: Coordinate? = null,
    val questRadius: Double? = null,
    val questList: List<QuestItem>? = null
)



