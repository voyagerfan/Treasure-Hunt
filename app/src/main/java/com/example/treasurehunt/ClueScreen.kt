/*
ClueScreen.kt

Lamar Petty
OSU
CS492

 */
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.R
import com.example.treasurehunt.TimerScreen
import com.example.treasurehunt.data.TreasureUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClueScreen(
    onFoundItClick: () -> Unit,
    onHintClick: () -> Unit,
    treasureUIstate: TreasureUiState,
    timerValue: Int,
    onQuitClick: () -> Unit
) {
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

@Preview(showBackground = true)
@Composable
fun previewClueScreen() {
    ClueScreen(
        onFoundItClick = {},
        onHintClick = {},
        treasureUIstate = TreasureUiState(),
        timerValue = 0,
        onQuitClick = {}
    )
}
