/*
TreasureUiState.kt

Lamar Petty
OSU
CS492
 */

package com.example.treasurehunt.data

import com.example.treasurehunt.model.Clue
import com.example.treasurehunt.model.Geo

/*
Data class to store UI state variable. It is manipulated in the viewmodel
 */

data class TreasureUiState(
    val isShowingHomePage: Boolean = true,
    val showHint: Boolean = false,
    val currentClue: Clue = DataSource.clue1,
    val currentGeo: Geo = DataSource.geo1,
    val currentLoc: MutableList<Double> = mutableListOf(0.0,0.0)

)

/*

 */
