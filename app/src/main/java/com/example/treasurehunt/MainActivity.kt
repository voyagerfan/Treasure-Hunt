/*
MainActivity.kt

Lamar Petty
OSU
CS492
 */

package com.example.treasurehunt

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.treasurehunt.data.ScreenList
import com.example.treasurehunt.screens.AchievementsScreen
import com.example.treasurehunt.screens.HomeScreen
import com.example.treasurehunt.screens.OnboardingScreen
import com.example.treasurehunt.screens.PlayGameScreen
import com.example.treasurehunt.screens.RuleScreen
import com.example.treasurehunt.screens.StartGameScreen
import com.example.treasurehunt.ui.theme.TreasureHuntTheme
import com.example.treasurehunt.utils.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TreasureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        checkAndUpdateFinePermission()
        checkAndUpdateCoarsePermission()
        setContent {
            TreasureHuntTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = ScreenList.ONBOARDING.name) {
                        composable(route = ScreenList.ONBOARDING.name) {
                            OnboardingScreen(
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                        composable(route = ScreenList.RULE_SCREEN.name) {
                            RuleScreen(navController = navController)
                        }
                        composable(route = ScreenList.HOME_SCREEN.name) {
                            HomeScreen(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                        composable(route = ScreenList.ACHIEVEMENTS_SCREEN.name) {
                            AchievementsScreen()
                        }
                        composable(route = ScreenList.START_SCREEN.name) {
                            StartGameScreen(
                                onBackArrowPressed = {navController.navigate(route = ScreenList.HOME_SCREEN.name)}
                            )
                        }
                        composable(route = ScreenList.PLAY_GAME_SCREEN.name) {
                            val timerValue by viewModel.timer.collectAsState()
                            val treasureUiState by viewModel.uiState.collectAsState()
                            val locationState by viewModel.locationLoadingState.collectAsState()
                            PlayGameScreen(
                                locationState = locationState,
                                viewModel = viewModel,
                                onFoundItClick = {
                                    if (viewModel.locationLoadingState.value is Response.Idle) {
                                        viewModel.pauseTimer()
                                        viewModel.getCurrentLocation()
                                    }
                                },
                                onHintClick = {
                                    viewModel.hintClicked()
                                },
                                treasureUIstate = treasureUiState,
                                timerView = {
                                    TimerScreen(timerValue = timerValue)
                                },
                                onQuitClick = {
                                    viewModel.stopTimer()
                                    navController.navigate(route = ScreenList.HOME_SCREEN.name)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == 1 && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.updateFinePermissionState(isGranted = true)
                viewModel.updateCoarsePermissionState(isGranted = true)
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.updateCoarsePermissionState(isGranted = true)
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )) {
                viewModel.updatePermissionRationaleState(shouldShow = true)
                viewModel.updatePermissionDenialCount()
            } else {
                viewModel.updatePermissionDenialCount()
            }
        }
    }

    private fun checkAndUpdateFinePermission() {
        val isFineGranted = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        viewModel.updateFinePermissionState(isGranted = isFineGranted)
    }

    private fun checkAndUpdateCoarsePermission() {
        val isCoarseGranted = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        viewModel.updateCoarsePermissionState(isGranted = isCoarseGranted)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TreasureHuntTheme {
        TreasureHuntApp()
    }
}
