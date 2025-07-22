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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.treasurehunt.model.Coordinate
import com.example.treasurehunt.ui.ScreenList
import com.example.treasurehunt.ui.screens.AchievementsScreen
import com.example.treasurehunt.ui.screens.EndGameScreen
import com.example.treasurehunt.ui.screens.HomeScreen
import com.example.treasurehunt.ui.screens.OnboardingScreen
import com.example.treasurehunt.ui.screens.PlayGameScreen
import com.example.treasurehunt.ui.screens.RuleScreen
import com.example.treasurehunt.ui.screens.StartGameScreen
import com.example.treasurehunt.ui.theme.TreasureHuntTheme
import com.example.treasurehunt.utils.AppUtils
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
                    val timerValue by viewModel.timer.collectAsState()
                    val treasureUiState by viewModel.uiState.collectAsState()
                    val startGameState by viewModel.gameStartScreenState.collectAsState()

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
                                questItemList = startGameState.questList,
                                onBackArrowPressed = { navController.navigate(route = ScreenList.HOME_SCREEN.name) },
                                questQuery = { query ->
                                    val coordinates = Coordinate(latitude = treasureUiState.currentLoc[0], longitude = treasureUiState.currentLoc[1]) // TODO: refactor TreasureUIState to use Coordinate
                                    val searchParams = query.copy(originCoordinates = coordinates)
                                    viewModel.getQuestItems(searchParams)
                                },
                                questSelected = { userSelectedQuest ->
                                    viewModel.updateUserQuest(userSelectedQuest)
                                    navController.navigate(route = ScreenList.PLAY_GAME_SCREEN.name)
                                }
                            )
                        }
                        composable(route = ScreenList.PLAY_GAME_SCREEN.name) {
                            viewModel.fetchGameCompletedImage("test")
                            val locationState by viewModel.locationLoadingState.collectAsState()
                            if(treasureUiState.isGameCompleted) { navController.navigate(route = ScreenList.END_GAME_SCREEN.name) }
                            PlayGameScreen(
                                locationState = locationState,
                                treasureUIstate = treasureUiState,
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
                                timerView = {
                                    TimerScreen(timerValue = timerValue)
                                },
                                onQuitClick = {
                                    viewModel.stopTimer()
                                    navController.navigate(route = ScreenList.HOME_SCREEN.name)
                                }
                            )
                        }
                        composable(route = ScreenList.END_GAME_SCREEN.name) {
                            EndGameScreen(
                                treasureUIstate = treasureUiState,
                                elapsedTime = timerValue,
                                attemptCount = viewModel.getAttemptCount(),
                                distance = AppUtils.haversine(
                                    destination = treasureUiState.currentQuest!!.coordinates,
                                    origin = treasureUiState.currentLoc
                                ),
                                onHomeClick = {
                                    viewModel.updateGameCompleted(false)
                                    viewModel.stopTimer()
                                    viewModel.resetQueue()
                                    viewModel.updateLoadingStateToIdle()
                                    navController.navigate(route = ScreenList.HOME_SCREEN.name)
                                },

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
