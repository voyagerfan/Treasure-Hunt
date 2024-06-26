/*
Lamar Petty
OSU
CS 492
 */

/*
References:
https://medium.com/@TippuFisalSheriff/creating-a-timer-screen-with-kotlin-and-jetpack-compose-in-android-f7c56952d599
 */

package com.example.treasurehunt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.treasurehunt.data.DataSource
import com.example.treasurehunt.data.TreasureUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TreasureViewModel : ViewModel() {
    val _uiState = MutableStateFlow(TreasureUiState())
    val uiState: StateFlow<TreasureUiState> = _uiState.asStateFlow()

    //timer variables
    val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()
    var timerJob: Job? = null



    fun testFunc(input: Boolean){
        _uiState.update{
            it.copy(
                isShowingHomePage = false
            )
        }
    }
    // toggle the hint button
    fun hintClicked(){
        if(!uiState.value.showHint){
            _uiState.update{
                it.copy(
                    showHint = true
                )
            }
        }else{
            _uiState.update{
                it.copy(
                    showHint = false
                )
            }
        }
    }

    // Update the clue and geo after first clue solved
    fun updateClueAndGeo(){
        _uiState.update{
            it.copy(
                currentClue = DataSource.clue2,
                currentGeo = DataSource.geo2
            )
        }
    }

    fun updateCurrentLoc(lat: Double, lon: Double){
        _uiState.update {
            it.copy(
                currentLoc = mutableListOf(lat, lon)
            )
        }
    }

    fun resetCurrentLoc(){
        _uiState.update {
            it.copy(
                currentLoc = mutableListOf(0.0, 0.0)
            )
        }
    }




    /**************
    Timeer functions
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