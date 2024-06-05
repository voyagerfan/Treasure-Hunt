/*
ClueSolved.kt

Lamar Petty
OSU
CS 492
 */

/*
 * Citation:
 * https://discuss.kotlinlang.org/t/format-a-double-to-fixed-decimal-length/20074
 */

package com.example.treasurehunt



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.treasurehunt.data.TreasureUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClueSolvedScreen(
    onContinueClick: () -> Unit,
    timerValue: Int,
    distance: Double,
    treasureUiState: TreasureUiState
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // add the painter resource
            val image = painterResource(treasureUiState.currentClue.picture)
            // Truncate the double to 3 decimals
            var truncDist = String.format("%.3f", distance)

            Text(
                text = "Great Job!\n\n You are $truncDist km from the destination",
                textAlign = TextAlign.Center)

            Text(
                text = stringResource(R.string.ContinueInstruction),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onContinueClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.Continue))
            }
            TimerScreen(timerValue = timerValue)

            Text(
                text = stringResource(treasureUiState.currentClue.clueDetail),
                textAlign = TextAlign.Center
            )
            Box {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color.Blue)
                )
            }

        }
    }
}

@Preview
@Composable
fun previewClueSolved() {
    ClueSolvedScreen(
        onContinueClick = {},
        timerValue = 1000,
        distance = 0.2,
        treasureUiState = TreasureUiState()
    )
}