package com.example.treasurehunt.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.treasurehunt.R
import com.example.treasurehunt.TimerScreen
import com.example.treasurehunt.TreasureViewModel
import com.example.treasurehunt.data.TreasureUiState
import com.example.treasurehunt.model.AttemptList
import com.example.treasurehunt.utils.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartGameScreen(
    viewModel: TreasureViewModel = hiltViewModel(),
    onFoundItClick: () -> Unit,
    onHintClick: () -> Unit,
    treasureUIstate: TreasureUiState,
    timerValue: Int,
    onQuitClick: () -> Unit
) {
    val locationState by viewModel.locationLoadingState.collectAsState()
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
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "BY LP"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.CluePrefix) + stringResource(treasureUIstate.currentClue.clueText)
            )
            Text(
                text = stringResource(R.string.NeedHint)
            )
            Button(
                onClick = onHintClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (!treasureUIstate.showHint) {
                    Text(stringResource(R.string.Hint))
                } else {
                    Text(stringResource(R.string.HideHint))
                }
            }
            // if the UI state property showHint == True, show the hint
            if (treasureUIstate.showHint) {
                Text(
                    text = stringResource(R.string.HintPrefix) + stringResource(treasureUIstate.currentClue.clueHint)
                )
            }
            Button(
                onClick = onFoundItClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.FoundIt))
            }

            TimerScreen(timerValue = timerValue)
            if (locationState !is Response.Idle) {
                when(val currentState = locationState) {
                    is Response.Loading -> CircularProgressIndicator()
                    is Response.Success -> {
                        TimedResponse(
                            displayTime = 3000,
                            message = "Distance from Destination: ${currentState.data}"
                        ) { isFinished ->
                            if(isFinished) {
                                timedResponseComplete = true
                                viewModel.updateLoadingStateToIdle()
                            }
                        }
                    }
                    else -> {}
                }
            }

            HistoryTable(
                attemptHistory = if(timedResponseComplete) viewModel.getCurrentAttemptQueue() else ArrayDeque()
            )

            Spacer(modifier = Modifier.height(200.dp))
            Button(
                onClick = onQuitClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Quit")
            }

        }
    }
}

@Composable
fun HistoryTable(
    attemptHistory: ArrayDeque<AttemptList>
) {
    attemptHistory.forEach { attempt ->
        Row {
            Text(attempt.attemptNumber.toString())
            Spacer(modifier = Modifier.padding(10.dp))
            Text(attempt.distance.toString())
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
        Text(message)
    } else {
        isFinished(true)
    }
}
