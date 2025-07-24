package com.example.treasurehunt.ui.state

import com.example.treasurehunt.GetGreetingQuery
import com.example.treasurehunt.GetQuestItemsQuery
import com.example.treasurehunt.model.Coordinate
import com.example.treasurehunt.model.QuestItem
import com.example.treasurehunt.utils.Response

data class StartGameState(
    val greetings: Response<GetGreetingQuery.Data>? = null,
    val currentLocation: Coordinate? = null,
    val questRadius: Double? = null,
    val questList: Response<GetQuestItemsQuery.Data>? = null
)




