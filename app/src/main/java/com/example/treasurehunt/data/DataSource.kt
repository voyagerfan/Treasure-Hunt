/*
DataSource.kt

Lamar Petty
OSU
CS492
 */

package com.example.treasurehunt.data

import androidx.annotation.StringRes
import com.example.treasurehunt.R
import com.example.treasurehunt.model.CompletedQuestData
import com.example.treasurehunt.model.Coordinate
import com.example.treasurehunt.model.QuestImage
import com.example.treasurehunt.model.QuestItem
import com.example.treasurehunt.model.Rule

/*
DataSource object to start all of the data
 */

object DataSource {

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
}

data class PermissionRationale(
    @StringRes val title: Int,
    @StringRes val text: Int
)

val rationale = PermissionRationale(
    title = R.string.alert_dialog_title,
    text = R.string.alert_dialog_text
)

/* Temporary hardcode list of QuestCards for testing */
val questList = listOf(
    QuestItem(
        title = "The Roar on the Wind",
        description = "Feel the thunder of fearless riders chasing time along a road that bends reality.",
        clue = "Once a year, a fast motorcycle can be found competing on public roads in what is wildy regarded as the most dangerous race in the world. Where is this motorcycle?",
        hint = "The event is held on an island in the Irish sea",
        coordinates = Coordinate(54.1672, -4.4780),
        rating = 3.8,
        endGameAssets = CompletedQuestData(
            questDetail = R.string.ClueDetail1,
            questPicture = QuestImage.Resource(resId = R.drawable.isleofman), // TODO: Remove when ImageService is integrated
            questImageID = ""
        )
    ),
    QuestItem(
        title = "Hollowed from Stone, Raised by Faith",
        description = "Step into silent temples carved deep by hands guided more by spirit than by tools",
        clue = "This rock-hewn church and UNESCO site was built around the 12th century and was of great spiritual importance then and even today. There are 10 more close by. Where is this church?",
        hint = "The oldest human remains known to man share the same country with this church.",
        coordinates = Coordinate(12.0317, 39.0411),
        rating = 4.3,
        endGameAssets = CompletedQuestData(
            questDetail = R.string.ClueDetail2,
            questPicture = QuestImage.Resource(resId = R.drawable.lalibela), // TODO: Remove when ImageService is integrated
            questImageID = ""
        )
    )
)
