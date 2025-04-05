/*
Indicator.kt

Lamar Petty
OSU
CS 492
 */

package com.example.treasurehunt


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/*
 * Citation:
 * https://stackoverflow.com/questions/73966501/circular-loading-spinner-in-jetpack-compose
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScreen(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "TREASURE HUNT")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "BY LP",
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "FINDING LOCATION",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))

            Indicator()
        }
    }
    var context = LocalContext.current
}



@Composable
fun Indicator(
    size: Dp = 300.dp, // indicator size
    sweepAngle: Float = 90f, // angle (lenght) of indicator arc
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth, //width of circle and ar lines
) {

    ////// animation //////

    // docs recomend use transition animation for infinite loops
    // https://developer.android.com/jetpack/compose/animation
    val transition = rememberInfiniteTransition()

    // define the changing value from 0 to 360.
    // This is the angle of the beginning of indicator arc
    // this value will change over time from 0 to 360 and repeat indefinitely.
    // it changes starting position of the indicator arc and the animation is obtained
    val currentArcStartAngle by transition.animateValue(
        initialValue = 0,
        targetValue = 360,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        )
    )

    ////// draw /////

    // define stroke with given width and arc ends type considering device DPI
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    // draw on canvas
    Canvas(
        modifier =
            Modifier
                .size(size) // canvas size
    ) {
        // draw "background" (gray) circle with defined stroke.
        // without explicit center and radius it fit canvas bounds
        drawCircle(androidx.compose.ui.graphics.Color.LightGray, style = stroke)

        // draw arc with the same stroke
        drawArc(
            color = androidx.compose.ui.graphics.Color.Blue,
            // arc start angle
            // -90 shifts the start position towards the y-axis
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun previewIndicator() {
    LoadingScreen()
}
