package com.example.treasurehunt.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.treasurehunt.R
import com.example.treasurehunt.TreasureViewModel
import com.example.treasurehunt.data.TreasureUiState
import com.example.treasurehunt.model.AttemptList
import com.example.treasurehunt.ui.theme.catamaranFamily
import com.example.treasurehunt.utils.Response
import kotlinx.coroutines.delay


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayGameScreen(
    locationState: Response<Double>,
    viewModel: TreasureViewModel = hiltViewModel(),
    onFoundItClick: () -> Unit,
    onHintClick: () -> Unit,
    treasureUIstate: TreasureUiState,
    timerView: @Composable () -> Unit,
    onQuitClick: () -> Unit
) {
    var timedResponseComplete by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.AppTitle))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onQuitClick,
                        modifier = Modifier
                    ) {
                        Text(
                            text = "Quit",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.CluePrefix) + stringResource(treasureUIstate.currentClue.clueText),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = stringResource(R.string.NeedHint),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Button(
                    onClick = onHintClick,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (!treasureUIstate.showHint) {
                        Text(
                            text = stringResource(R.string.Hint),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.HideHint),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                // if the UI state property showHint == True, show the hint
                if (treasureUIstate.showHint) {
                    Text(
                        text = stringResource(R.string.HintPrefix) + stringResource(treasureUIstate.currentClue.clueHint),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Button(
                    onClick = onFoundItClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.FoundIt),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                timerView()

                Box(
                    modifier = Modifier.height(48.dp)
                ) {
                    if (locationState !is Response.Idle) {
                        when (val currentState = locationState) {
                            is Response.Loading -> CircularProgressIndicator()
                            is Response.Success -> {
                                TimedResponse(
                                    displayTime = 3000,
                                    message = "Current Distance: ${currentState.data}"
                                ) { isFinished ->
                                    if (isFinished) {
                                        timedResponseComplete = true
                                        viewModel.updateLoadingStateToIdle()
                                    }
                                }
                            }
                            else -> {}
                        }
                    } else {
                        viewModel.startTimer()
                    }
                }

                HistoryTable(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(bottom = 10.dp),
                    attemptHistory = if (timedResponseComplete) viewModel.getCurrentAttemptQueue() else ArrayDeque()
                )
            }
        }
    }
}

@Composable
fun HistoryTable(
    modifier: Modifier,
    attemptHistory: ArrayDeque<AttemptList>
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Attempt",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
                )
            Text(text = "Distance from Destination (km)",
                modifier = Modifier.weight(2f),
                fontFamily = catamaranFamily,
                fontSize = 20.sp)
        }
        LazyColumn {
            items(attemptHistory) { attempt ->
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = attempt.attemptNumber.toString(),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = dynamicColorByDistance(attempt.distance)

                    )
                    Text(
                        text = attempt.distance.toString(),
                        Modifier.weight(2f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = dynamicColorByDistance(attempt.distance)
                    )
                }
            }
        }
    }
}

@Composable
fun TimedResponse(
    displayTime: Long,
    message: String,
    isFinished: (Boolean) -> Unit
) {
    require(displayTime >= 0) { "Seconds must be greater than or equal to 0" }
    var millis by remember { mutableLongStateOf(displayTime) }

    LaunchedEffect(displayTime) {
        while (millis > 0) {
            delay(1000)
            millis -= 1000
        }
    }
    if (millis > 0) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    } else {
        isFinished(true)
    }
}

fun dynamicColorByDistance(distance: Double): Color {
    return when (distance) {
        in 25.0..50.0 -> Color.Yellow
        in 50.1..99.9 -> Color(0xFFFFA500)
        in 100.0..Double.MAX_VALUE -> Color.Red
        else -> Color.Black
    }
}