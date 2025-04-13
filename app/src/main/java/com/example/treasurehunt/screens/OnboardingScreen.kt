package com.example.treasurehunt.screens

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.treasurehunt.TreasureViewModel
import com.example.treasurehunt.data.ScreenList
import com.example.treasurehunt.data.rationale

@Composable
fun OnboardingScreen(
    viewModel: TreasureViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiStatePermissions.collectAsState()
    val context = LocalContext.current
    val activity = context as ComponentActivity

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = "Hello from TreasureHunt",
            fontSize = 30.sp
        )

        if (uiState.permissionDenialCount < 2 && (!uiState.isFineAccessGranted && !uiState.isCoarseAccessGranted)) {
            if (!uiState.showPermissionRationale) {
                Button(
                    onClick = {
                        activity.requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            1
                        )
                    }
                ) {
                    Text(text = "Continue")
                }
            }
            if (uiState.showPermissionRationale) {
                PermissionAlertDialogBox(
                    onDismissRequest = { viewModel.updatePermissionRationaleState(shouldShow = false) },
                    onConfirmation = { viewModel.updatePermissionRationaleState(shouldShow = false) },
                    rationale = rationale
                )
            }
        } else if (uiState.isFineAccessGranted || uiState.isCoarseAccessGranted) {
            navController.navigate(route = ScreenList.RULE_SCREEN.name)
        } else {
            Button(
                onClick = { activity.finish() }
            ) {
                Text(text = "Exit")
            }
        }
    }
}
