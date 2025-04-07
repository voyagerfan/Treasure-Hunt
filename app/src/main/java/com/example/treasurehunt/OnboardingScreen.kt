package com.example.treasurehunt


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    viewModel: TreasureViewModel
) {
    val uiState by viewModel.uiStatePermissions.collectAsState()
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

        Text(
            text = "Current State of Coarse Access = ${uiState.isCoarseAccessGranted}",
            fontSize = 20.sp
        )
        Text(
            text = "Current State of Fine Access = ${uiState.isCoarseAccessGranted}",
            fontSize = 20.sp
        )
    }
}