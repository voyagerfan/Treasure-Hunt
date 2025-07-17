/*
TreasureUiState.kt

Lamar Petty
OSU
CS492
 */

package com.example.treasurehunt.ui.state

import com.example.treasurehunt.model.QuestImage
import com.example.treasurehunt.model.QuestItem

/*
Data class to store UI state variable. It is manipulated in the viewmodel
 */

data class TreasureUiState(
    val showHint: Boolean = false,
    val currentLoc: MutableList<Double> = mutableListOf(0.0, 0.0),
    val currentQuest: QuestItem? = null,
    val completeQuestImage: QuestImage? = null,
    val isGameCompleted: Boolean = false
)

data class PermissionUiState(
    val isFineAccessGranted: Boolean = false,
    val isCoarseAccessGranted: Boolean = false,
    val showPermissionRationale: Boolean = false,
    val permissionDenialCount: Int = 0
)
