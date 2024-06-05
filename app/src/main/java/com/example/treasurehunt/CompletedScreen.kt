/*
CompletedScreen.kt

Lamar Petty
OSU
CS 492
 */

package com.example.treasurehunt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.data.TreasureUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedScreen(
    treasureUIstate: TreasureUiState,
    distance: Double,
    elapsedTime: Int,
    onHomeClick: ()->Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.AppTitle))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "BY LP",
                )
            }
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            // add the painter resource
            val image = painterResource(treasureUIstate.currentClue.picture)
            // trucate the double
            var truncDist = String.format("%.3f", distance)
            Text(
                text = "Awesome Job Completing the Game\n\nYou are $truncDist km from the destination",
                textAlign = TextAlign.Center)

            Text(
                text = stringResource(treasureUIstate.currentClue.clueDetail),
                textAlign = TextAlign.Center)

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = stringResource(R.string.TotalTime),
                    textAlign = TextAlign.Center
                )

                TimerScreen(timerValue = elapsedTime)

            }
            Box {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color.Blue)
                )
            }

            Button(
                onClick = onHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("HOME")
            }
        }
    }
}

@Preview
@Composable
fun previewCompleted() {
    CompletedScreen(
        treasureUIstate = TreasureUiState(true, false, DataSource.clue1, DataSource.geo1),
        distance = 0.2,
        elapsedTime = 900,
        onHomeClick = {}
    )
}
