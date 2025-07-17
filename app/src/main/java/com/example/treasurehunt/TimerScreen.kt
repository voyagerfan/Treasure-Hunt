/*
TimerScreen.kt

Lamar Petty
OSU
CS 492

References:
https://stackoverflow.com/questions/65637680/kotlin-check-if-a-variable-is-between-two-numbers
https://medium.com/@TippuFisalSheriff/creating-a-timer-screen-with-kotlin-and-jetpack-compose-in-android-f7c56952d599
*/
package com.example.treasurehunt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlin.math.floor

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    timerValue: Int
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = modifier,
            style = textStyle ?: MaterialTheme.typography.bodyMedium,
            text = formatTime(timerValue)
        )
    }
}

/**
 * Calculate the minutes and seconds base on the seconds passed in (time)
 * Take the floor function to get the actual minutes
 *
 * @param time The starting coordinate (latitude and longitude).
 * @return formatted string of minutes:seconds.

 */
fun formatTime(time: Int): String {
    var minutes: Any = 0.00
    minutes as Double
    minutes = time
    minutes = floor(minutes / 60.0)
    minutes = minutes.toInt()

    /*
    Roll the seconds back to 0 if it reaches 60 and
    the 60 second interval has been reached.
     */
    var seconds = time
    if (time >= 60) {
        seconds = time - (minutes * 60)
    }
    if (seconds in 0..9) {
        return "$minutes : 0$seconds"
    } else {
        return "$minutes : $seconds"
    }
}
