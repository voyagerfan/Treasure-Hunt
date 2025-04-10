/*
LocationPermissionScreen.kt

Lamar Petty
OSU
CS492

Citation:
https://github.com/android/platform-samples/blob/main/samples/location/src/main/java/com/example/platform/location/permission/LocationPermissionsScreen.kt
 */

package com.example.treasurehunt

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
// import com.google.android.catalog.framework.annotations.Sample

/*
@RequiresApi(Build.VERSION_CODES.Q)
@Sample(
    name = "Location - Permissions",
    description = "This Sample demonstrate best practices for Location Permission",
    documentation = "https://developer.android.com/training/location/permissions",
    tags = ["permissions"],
)*/
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    context: Context,
    locationPermissionState: PermissionState,
    fineLocationPermissionState: MultiplePermissionsState,
    bgLocationPermissionState: PermissionState,
    rationaleState: RationaleState?
) {
    var rationaleState = rationaleState
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show rationale dialog when needed
            rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

            PermissionRequestButton(
                isGranted = locationPermissionState.status.isGranted,
                title = "Approximate location access"
            ) {
                if (locationPermissionState.status.shouldShowRationale) {
                    rationaleState = RationaleState(
                        title = "Request approximate location access",
                        rationale = "In order to use this feature please grant access by accepting " +
                            "the location permission dialog." + "\n\nWould you like to continue?"
                    ) { proceed ->
                        if (proceed) {
                            locationPermissionState.launchPermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    locationPermissionState.launchPermissionRequest()
                }
            }

            PermissionRequestButton(
                isGranted = fineLocationPermissionState.allPermissionsGranted,
                title = "Precise location access"
            ) {
                if (fineLocationPermissionState.shouldShowRationale) {
                    rationaleState = RationaleState(
                        title = "Request Precise Location",
                        rationale = "In order to use this feature please grant access by accepting " +
                            "the location permission dialog." + "\n\nWould you like to continue?"
                    ) { proceed ->
                        if (proceed) {
                            fineLocationPermissionState.launchMultiplePermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    fineLocationPermissionState.launchMultiplePermissionRequest()
                }
            }

            // Background location permission needed from Android Q,
            // before Android Q, granting Fine or Coarse location access automatically grants Background
            // location access

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PermissionRequestButton(
                    isGranted = bgLocationPermissionState.status.isGranted,
                    title = "Background location access"
                ) {
                    if (locationPermissionState.status.isGranted || fineLocationPermissionState.allPermissionsGranted) {
                        if (bgLocationPermissionState.status.shouldShowRationale) {
                            rationaleState =
                                RationaleState(
                                    title = "Request background location",
                                    rationale = "In order to use this feature please grant access by " +
                                        "accepting the background location permission dialog." +
                                        "\n\nWould you like to continue?"
                                ) { proceed ->
                                    if (proceed) {
                                        bgLocationPermissionState.launchPermissionRequest()
                                    }
                                    rationaleState = null
                                }
                        } else {
                            bgLocationPermissionState.launchPermissionRequest()
                        }
                    } else {
                        Toast
                            .makeText(
                                context,
                                "Please grant either Approximate location access permission or Fine" + "location access permission",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS)) }
        ) {
            Icon(Icons.Outlined.Settings, "Location Settings")
        }
    }
}
