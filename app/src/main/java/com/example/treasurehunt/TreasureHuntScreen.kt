/*
Lamar Petty
OSU
CS 492
 */
package com.example.treasurehunt

/*
 * Citations:
 * https://github.com/android/platform-samples/blob/main/samples/location/src/main/java/com/example/platform/location/locationupdates/LocationUpdatesScreen.kt#L162
 * https://www.geeksforgeeks.org/automatic-dialog-dismiss-in-android/
 * https://stackoverflow.com/questions/74606139/how-to-create-a-location-request
 */

import ClueScreen
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Looper
import android.widget.Button
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.treasurehunt.model.Geo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

enum class TreasureHuntScreen(
    @StringRes val title: Int
) {
    PermissionScreen(title = R.string.PermissionScreen),
    StartScreen(title = R.string.StartScreen),
    ClueScreen(title = R.string.DetailScreen),
    LoadingScreen(title = R.string.LoadingScreen),
    ClueSolvedScreen(title = R.string.ClueSolved),
    CompletedScreen(title = R.string.CompletedScreen)
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TreasureHuntApp(
    viewModel: TreasureViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    // Approximate location access is sufficient for most of use cases
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // When precision is important request both permissions but make sure to handle the case where
    // the user only grants ACCESS_COARSE_LOCATION
    val fineLocationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    // In really rare use cases, accessing background location might be needed.
    val bgLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    // Keeps track of the rationale dialog state, needed when the user requires further rationale
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    // collect the UI state (for clue and hints)
    val uiState by viewModel.uiState.collectAsState()

    // Collect the timer state.
    val timerValue by viewModel.timer.collectAsState()

    val activity = context as Activity

    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(activity)
    }

    // location request is used with DisposableEffect on the ClueScreen to trigger more
    // frequent updates.
    var locationRequest = LocationRequest
        .Builder(100, 100)
        .setMaxUpdateAgeMillis(0)
        .build()

    var onUpdate = { result: LocationResult -> Unit }
    val currentOnUpdate by rememberUpdatedState(newValue = onUpdate)
    var lifecycleOwner = LocalLifecycleOwner.current
    var locationInfo by remember { mutableStateOf(mutableListOf(0.0, 0.0)) }
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = TreasureHuntScreen.PermissionScreen.name
    ) {
        composable(route = TreasureHuntScreen.PermissionScreen.name) {
            // Launch the permission screen composable
            LocationPermissionScreen(
                context = context,
                locationPermissionState = locationPermissionState,
                fineLocationPermissionState = fineLocationPermissionState,
                bgLocationPermissionState = bgLocationPermissionState,
                rationaleState = rationaleState
            )
            // if access is granted, navigate to the start screen.
            if (locationPermissionState.status.isGranted or fineLocationPermissionState.allPermissionsGranted) {
                navController.navigate(TreasureHuntScreen.StartScreen.name)
            }
        }

        composable(route = TreasureHuntScreen.StartScreen.name) {
            StartScreen(
                onStartClick = {
                    navController.navigate(TreasureHuntScreen.ClueScreen.name)
                    viewModel.startTimer()
                }
            )
        }

        composable(route = TreasureHuntScreen.ClueScreen.name) {
            // Added disposable effect for more frequent updates.
            DisposableEffect(lifecycleOwner) {
                val locationClient = LocationServices.getFusedLocationProviderClient(context)
                val locationCallback: LocationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        currentOnUpdate(result)
                    }
                }
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        locationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    } else if (event == Lifecycle.Event.ON_STOP) {
                        locationClient.removeLocationUpdates(locationCallback)
                    }
                }
                // Add the observer to the lifecycle
                lifecycleOwner.lifecycle.addObserver(observer)

                // When the effect leaves the Composition, remove the observer
                onDispose {
                    locationClient.removeLocationUpdates(locationCallback)
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            // new code end *****
            ClueScreen(
                onFoundItClick = {
                    // pause the timer
                    viewModel.pauseTimer()

                    // Coroutine to fetch the coordinates
                    scope.launch(Dispatchers.IO) {
                        // set the priority
                        var priority = Priority.PRIORITY_HIGH_ACCURACY
                        // build a location request
                        val locRequest = CurrentLocationRequest.Builder()
                            .setMaxUpdateAgeMillis(0)
                            .setPriority(priority)
                            .build()

                        // get the location
                        val result = locationClient.getCurrentLocation(locRequest, CancellationTokenSource().token).await()
                        // when await is complete, pass the lat and lon into the viewModel
                        result?.let { fetchedLocation ->

                            viewModel.updateCurrentLoc(fetchedLocation.latitude, fetchedLocation.longitude)
                        }
                    }

                    // Check to see if both are 0.0 (e.g. the await() hasn't returned yet
                    if (uiState.currentLoc[0] == 0.0 || uiState.currentLoc[1] == 0.0) {
                        navController.navigate(TreasureHuntScreen.LoadingScreen.name)
                    } else {
                        var distance_to_dest = haversine(uiState.currentGeo, uiState.currentLoc)
                        if (distance_to_dest < 0.5) {
                            navController.navigate(TreasureHuntScreen.ClueSolvedScreen.name)
                        } else {
                            navController.navigate(TreasureHuntScreen.LoadingScreen.name)
                        }
                    }
                },
                onHintClick = { viewModel.hintClicked() },
                treasureUIstate = uiState,
                timerValue = timerValue,
                onQuitClick = {
                    // stop the timer
                    viewModel.stopTimer()
                    // navigate back to the start screen
                    navController.navigate(TreasureHuntScreen.StartScreen.name)
                }
            )
        }
        composable(route = TreasureHuntScreen.LoadingScreen.name) {
            LoadingScreen()

            // wait for the deferred object to return.
            LaunchedEffect(Unit) {
                while (uiState.currentLoc[0] == 0.0) {
                    delay(1000)
                }
                // Once there is location data, calculate the distance and route accordingly
                if (uiState.currentLoc[0] != 0.0) {
                    var distanceToDest = haversine(uiState.currentGeo, uiState.currentLoc)

                    // if distance is in specification, go to ClueSolvedScreen or completed screen
                    if (distanceToDest < 0.2) {
                        if (uiState.currentClue.clueNumber == 1) {
                            navController.navigate(TreasureHuntScreen.ClueSolvedScreen.name)
                        } else {
                            navController.navigate(TreasureHuntScreen.CompletedScreen.name)
                        }
                    } else {
                        // present the Alert to the user
                        openAlertDialog(context = context)
                        // Reset the current location back to lat = 0.0, lon = 0.0
                        viewModel.resetCurrentLoc()
                        // restart timer
                        viewModel.startTimer()
                        // Navigate back to the ClueScreen
                        navController.navigate(TreasureHuntScreen.ClueScreen.name)
                    }
                }
            }
        }
        composable(route = TreasureHuntScreen.ClueSolvedScreen.name) {
            var distanceToDest = haversine(uiState.currentGeo, uiState.currentLoc)
            ClueSolvedScreen(
                onContinueClick = {
                    // Update the UI state Geo and Clue
                    viewModel.updateClueAndGeo()

                    // reset the current location to 0.0, 0.0
                    viewModel.resetCurrentLoc()

                    // Re-start the timer
                    viewModel.startTimer()

                    // Navigate back to the Clue screen (need to branch of done)
                    navController.navigate(TreasureHuntScreen.ClueScreen.name)
                },
                timerValue = timerValue,
                distance = distanceToDest,
                treasureUiState = uiState
            )
        }
        composable(route = TreasureHuntScreen.CompletedScreen.name) {
            var distanceToDest = haversine(uiState.currentGeo, uiState.currentLoc)
            CompletedScreen(
                treasureUIstate = uiState,
                distance = distanceToDest,
                elapsedTime = timerValue,
                onHomeClick = {
                    // reset the timer ot 0:00
                    viewModel.stopTimer()
                    // navigate back to the start screen
                    navController.navigate(TreasureHuntScreen.StartScreen.name)
                }
            )
        }
    }
}

// haversine formula for calculating distance
fun haversine(
    destination: Geo,
    Origin: List<Double>
): Double {
    // origin[0] = origin latitude
    // origin[1] = origin longitude
    val earthRadiusKm = 6372.8

    val dLat = Math.toRadians(destination.dLat - Origin[0])
    val dLon = Math.toRadians(destination.dLon - Origin[1])
    val originLat = Math.toRadians(Origin[0])
    val destinationLat = Math.toRadians(destination.dLat)
    val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat)
    val c = 2 * Math.asin(Math.sqrt(a))
    return earthRadiusKm * c
}

// Dialog box that opens if the user is the wrong location.
fun openAlertDialog(context: Context) {
    lateinit var alertDialog: AlertDialog
    lateinit var button: Button
    val dismissalDelayMillis = 3000 // 3 seconds

    // Create an AlertDialog.Builder
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Not there yet...")
    builder.setMessage("Please try again.")
    // Set a positive button and its click listener
    builder.setPositiveButton("OK") { dialog, which ->
        // Handle positive button click if needed
        dialog.dismiss()
    }
    // Create the AlertDialog
    alertDialog = builder.create()
    // Show the AlertDialog
    alertDialog.show()
    // Schedule the dismissal after a certain delay
    val handler = android.os.Handler()
    handler.postDelayed({
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }, dismissalDelayMillis.toLong())
}
