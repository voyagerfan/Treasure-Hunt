/*
Rule.kt

Lamar Petty
OSU
CS492
 */

package com.example.treasurehunt.model

import androidx.annotation.StringRes

/*
Data class to store the rule number and rule text. Used on the start page
 */
data class Rule(
    @StringRes val rule_number: Int,
    @StringRes val rule_text: Int
)
