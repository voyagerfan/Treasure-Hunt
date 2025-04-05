/*
Clue.kt

Lamar Petty
OSU
CS492
 */

package com.example.treasurehunt.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/*
Data class to store clue data.
 */

data class Clue(
    @StringRes val clueText: Int,
    @StringRes val clueHint: Int,
    @StringRes val clueDetail: Int,
    @DrawableRes val picture: Int,
    val clueNumber: Int
)
