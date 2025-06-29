/*
Lamar Petty
OSU
CS 492

References:
https://medium.com/@TippuFisalSheriff/creating-a-timer-screen-with-kotlin-and-jetpack-compose-in-android-f7c56952d599
 */

package com.example.treasurehunt

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.data.DataSource.geo1
import com.example.treasurehunt.data.PermissionUiState
import com.example.treasurehunt.data.TreasureHuntGraphQLService
import com.example.treasurehunt.data.TreasureUiState
import com.example.treasurehunt.model.AttemptList
import com.example.treasurehunt.model.Geo
import com.example.treasurehunt.model.QuestItem
import com.example.treasurehunt.model.StartGameState
import com.example.treasurehunt.utils.AppUtils
import com.example.treasurehunt.utils.Response
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class TreasureViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationRequest: CurrentLocationRequest,
    private val apolloClient: TreasureHuntGraphQLService
): ViewModel() {

    private val _locationLoadingState = MutableStateFlow<Response<Double>>(Response.Idle())
    val locationLoadingState: StateFlow<Response<Double>> = _locationLoadingState.asStateFlow()

    private var _currentAttemptQueue = MutableStateFlow(ArrayDeque<AttemptList>())
    private var attemptCount = mutableIntStateOf(0)

    private val _permissions = MutableStateFlow(PermissionUiState())
    val uiStatePermissions: StateFlow<PermissionUiState> = _permissions.asStateFlow()

    private val _uiState = MutableStateFlow(TreasureUiState())
    val uiState: StateFlow<TreasureUiState> = _uiState.asStateFlow()

    private val _gameStartScreenState = MutableStateFlow(StartGameState())
    val gameStartScreenState: StateFlow<StartGameState> = _gameStartScreenState.asStateFlow()

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()
    private var timerJob: Job? = null

    init {
        getGreetings()
    }

    fun getCurrentLocation() {
        _locationLoadingState.value = Response.Loading()
        viewModelScope.launch {
            val cancellationTokenSource = CancellationTokenSource()
            try {
                val getLocation = fusedLocationClient
                    .getCurrentLocation(
                        locationRequest,
                        cancellationTokenSource.token
                    )
                    .await() ?: throw IllegalStateException("Unable to determine Location")
                updateCurrentLoc(
                    lat = getLocation.latitude,
                    lon = getLocation.longitude
                )
                // calculations and updates
                attemptCount.intValue += 1
                val distance = AppUtils.haversine(
                    destination = _uiState.value.currentGeo,
                    origin = listOf(getLocation.latitude, getLocation.longitude),
                )
                Log.d("currentGeo", "${_uiState.value.currentGeo}")
                Log.d("origin", "${listOf(getLocation.latitude, getLocation.longitude)}")
                _currentAttemptQueue.value = ArrayDeque(_currentAttemptQueue.value).apply {
                    addFirst(
                        AttemptList(
                            attemptNumber = attemptCount.intValue,
                            distance = distance,
                        )
                    )
                }
                _locationLoadingState.value = Response.Success(data = distance)
            } catch (e: Exception) {
                _locationLoadingState.value = Response.Error(exception = e)
                logStackTrace(e)
            }
        }
    }

    fun updateLoadingStateToIdle() {
        _locationLoadingState.value = Response.Idle()
    }

    fun getCurrentAttemptQueue(): ArrayDeque<AttemptList> {
        return _currentAttemptQueue.value
    }

    fun getGreetings() {
        viewModelScope.launch {
            val fetchedGreetings = apolloClient.fetchGreetings().let { it?.filterNotNull()} ?: emptyList()
            _gameStartScreenState.update {
                it.copy(greetings = fetchedGreetings)
            }
        }
    }

    fun updateUserQuest(quest: QuestItem) {
        _uiState.update {
            it.copy(
                currentQuest = quest
            )
        }
    }

    fun updateGameCompleted(locationFound: Boolean) {
        _uiState.update {
            it.copy(
                isGameCompleted = locationFound
            )
        }
    }

    /*TODO: remove and rely only on questItem going forward*/
    fun updateCurrentGeoWithQuest(quest:QuestItem) {
        _uiState.update {
            it.copy(currentGeo = Geo(
                dLat = quest.coordinates.first,
                dLon = quest.coordinates.second
            ))
        }
    }

    fun getAttemptCount(): Int {
        return attemptCount.intValue
    }

    fun resetQueue() {
        _currentAttemptQueue.value.clear()
        attemptCount.intValue = 0
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
            it.copy(
                permissionDenialCount = uiStatePermissions.value.permissionDenialCount + 1
            )
        }
    }

    private fun logStackTrace(exception: Exception) {
        var current: Throwable? = exception
        var depth = 0
        while (current?.cause != null) {
            Log.e("Error", "Exception at level $depth: ${current.message}")
            current.stackTrace.forEachIndexed { index, element ->
                Log.e("$index", "  at ${element.className}.${element.methodName} (${element.fileName}:${element.lineNumber})")
            }
            current = current.cause
            depth++
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
