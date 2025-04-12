/*
Lamar Petty
OSU
CS 492

References:
https://medium.com/@TippuFisalSheriff/creating-a-timer-screen-with-kotlin-and-jetpack-compose-in-android-f7c56952d599
 */

package com.example.treasurehunt

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.data.PermissionUiState
import com.example.treasurehunt.data.TreasureUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreasureViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): ViewModel() {

    private val _permissions = MutableStateFlow(PermissionUiState())
    val uiStatePermissions: StateFlow<PermissionUiState> = _permissions.asStateFlow()

    private val _uiState = MutableStateFlow(TreasureUiState())
    val uiState: StateFlow<TreasureUiState> = _uiState.asStateFlow()

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()
    private var timerJob: Job? = null

    /**
     * Updates the backing property [_permissions] for the `isFineAccessGranted` based on the
     * value of [isGranted]
     *
     * @param [isGranted] A boolean value representing whether the fine access permission has been
     * granted.
     */
    internal fun updateFinePermissionState(isGranted: Boolean) {
        _permissions.update {
            it.copy(isFineAccessGranted = isGranted)
        }
    }
    /**
     * Updates the backing property [_permissions] for the `isCoarseAccessGranted` property based on
     * the value of [isGranted]
     *
     * @param [isGranted] A boolean value representing whether the fine access permission has been
     * granted.
     */
    internal fun updateCoarsePermissionState(isGranted: Boolean) {
        _permissions.update {
            it.copy(isCoarseAccessGranted = isGranted)
        }
    }

    internal fun updatePermissionRationaleState(shouldShow: Boolean) {
        _permissions.update {
            it.copy(showPermissionRationale = shouldShow)
        }
    }
    internal fun updatePermissionDenialCount() {
        _permissions.update {
            it.copy(permissionDenialCount =
                uiStatePermissions.value.permissionDenialCount + 1)
        }
    }

    fun hintClicked() {
        if (!uiState.value.showHint) {
            _uiState.update {
                it.copy(
                    showHint = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    showHint = false
                )
            }
        }
    }

    // Update the clue and geo after first clue solved
    fun updateClueAndGeo() {
        _uiState.update {
            it.copy(
                currentClue = DataSource.clue2,
                currentGeo = DataSource.geo2
            )
        }
    }

    fun updateCurrentLoc(
        lat: Double,
        lon: Double
    ) {
        _uiState.update {
            it.copy(
                currentLoc = mutableListOf(lat, lon)
            )
        }
    }

    fun resetCurrentLoc() {
        _uiState.update {
            it.copy(
                currentLoc = mutableListOf(0.0, 0.0)
            )
        }
    }

/**************
Timer functions
**************/
    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun stopTimer() {
        _timer.value = 0
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

/*

    val packageManager = applicationContext.packageManager


    private val _permissions = MutableStateFlow(PermissionUiState())
    val uiStatePermissions: StateFlow<PermissionUiState> = _permissions.asStateFlow()

    init {

        checkAndUpdatePermissions()
    }
    private fun checkAndUpdatePermissions() {
        _permissions.update {
            it.copy(isCoarseAccessGranted = ContextCompat
                .checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        _permissions.update {
            it.copy(
                isFineAccessGranted = ContextCompat
                    .checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
            )
        }
    }
 */
