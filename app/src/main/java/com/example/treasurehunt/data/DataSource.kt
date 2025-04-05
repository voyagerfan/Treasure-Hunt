/*
DataSource.kt

Lamar Petty
OSU
CS492
 */

package com.example.treasurehunt.data

import com.example.treasurehunt.R
import com.example.treasurehunt.model.Clue
import com.example.treasurehunt.model.Geo
import com.example.treasurehunt.model.Rule

/*
DataSource object to start all of the data
 */

object DataSource {
    val clue1 = Clue(
        clueText = R.string.cluetext1,
        clueHint = R.string.cluehint1,
        clueDetail = R.string.ClueDetail1,
        picture = R.drawable.isleofman,
        clueNumber = 1
    )

    val clue2 = Clue(
        clueText = R.string.cluetext2,
        clueHint = R.string.cluehint2,
        clueDetail = R.string.ClueDetail2,
        picture = R.drawable.lalibela,
        clueNumber = 2
    )

    fun loadRules(): List<Rule> {
        return listOf<Rule>(
            Rule(R.string.rule1, R.string.rule1_text),
            Rule(R.string.rule2, R.string.rule2_text),
            Rule(R.string.rule3, R.string.rule3_text),
            Rule(R.string.rule4, R.string.rule4_text),
            Rule(R.string.rule5, R.string.rule5_text),
            Rule(R.string.rule6, R.string.rule6_text),
            Rule(R.string.rule7, R.string.rule7_text),
            Rule(R.string.rule8, R.string.rule8_text),
            Rule(R.string.rule9, R.string.rule9_text)
        )
    }

    // Isle of man Grand Stand

    val geo1 = Geo(
        dLat = 54.1672,
        dLon = -4.4780
    )
    // Lalibela, church of St George

    val geo2 = Geo(
        dLat = 12.0317,
        dLon = 39.0411
    )
}
