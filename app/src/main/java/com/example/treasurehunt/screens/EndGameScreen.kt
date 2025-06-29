package com.example.treasurehunt.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.R
import com.example.treasurehunt.TimerScreen
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.data.TreasureUiState
import com.example.treasurehunt.data.questList
import com.example.treasurehunt.model.QuestImage
import com.example.treasurehunt.ui.theme.TreasureHuntTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndGameScreen(
    treasureUIstate: TreasureUiState,
    attemptCount: Int,
    distance: Double,
    elapsedTime: Int,
    onHomeClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.AppTitle))
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(
                        onClick = onHomeClick,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                    ) {
                        Text("HOME")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Awesome Job Completing:",
                style = MaterialTheme.typography.titleSmall
            )

            treasureUIstate.currentQuest?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            treasureUIstate.currentQuest?.endGameAssets?.let {
                Text(
                    text = stringResource(it.questDetail),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            Box {
                val questImage = treasureUIstate.currentQuest?.endGameAssets?.questPicture
                when (questImage) {
                    is QuestImage.Resource -> Image(
                        painter = painterResource(id = questImage.resId),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .background(Color.Blue)
                    )
                    is QuestImage.BitmapImage -> TODO()
                    is QuestImage.Url -> TODO()
                    null -> TODO()
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .wrapContentHeight()
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Stats",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Blue
                )
                StatRow(
                    title = "Attempts = ",
                    statValue = attemptCount.toString()
                )

                StatRow(
                    title = "Final Distance = ",
                    statValue = distance.toString()
                )

                StatRow(
                    title = stringResource(R.string.TotalTime),
                    content = { TimerScreen(timerValue = elapsedTime) }
                )
            }
        }
    }
}

@Composable
fun StatRow(
    title: String,
    statValue: String? = null,
    content: @Composable (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title.takeIf { statValue.isNullOrEmpty() } ?: (title + statValue),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.then(
                Modifier.takeIf { content != null && statValue == null }
                ?.padding(end = 8.dp)
                ?: Modifier
            )
        )
        if (content != null) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewEndGamesScreen() {
    TreasureHuntTheme {
        EndGameScreen (
            treasureUIstate = TreasureUiState(
                isShowingHomePage = true,
                showHint = false,
                currentClue = DataSource.clue1,
                currentGeo = DataSource.geo1,
                currentQuest = questList[0]
            ),
            distance = 0.2,
            elapsedTime = 900,
            attemptCount = 5,
            onHomeClick = {/* no logic, preview only*/}
        )
    }
}