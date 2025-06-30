package com.example.treasurehunt

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.treasurehunt.model.Coordinate
import com.example.treasurehunt.utils.AppUtils
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.random.Random


@RunWith(Parameterized::class)
class HaversineTestParameterized(
    private val destination: Coordinate,
    private val origin: List<Double>
) {
    /**
     * Verifies that the haversine function throws an exception when the latitude
     * is out of the valid range (-90.0 to 90.0).
     */
    @Test(expected = IllegalArgumentException::class)
    fun calculateDistance_returnsIllegalArgumentException() {
        AppUtils.haversine(destination, origin)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf(Coordinate(-90.1, 0.0), listOf(0.0, 1.0)),
            arrayOf(Coordinate(90.1, 0.0), listOf(1.0, 0.0)),
            arrayOf(Coordinate(0.0, -180.1), listOf(1.0, 1.0)),
            arrayOf(Coordinate(0.0, 180.1), listOf(1.0, 1.0))
        )
    }
}

@RunWith(AndroidJUnit4::class)
class HaversineUnitTest {

    /**
     * Verifies that the haversine implementation always yields distance >= 0
     */
    @Test
    fun calculateRandomDistance_returnGreaterOrEqualToZero() {
        repeat(100) {
            val destinationLatitude = Random.nextDouble(-90.0, 90.0)
            val destinationLongitude = Random.nextDouble(-180.0, 180.0)
            val originLatitude = Random.nextDouble(-90.0, 90.0)
            val originLongitude = Random.nextDouble(-180.0, 180.0)

            val distance = AppUtils.haversine(
                destination = Coordinate(destinationLatitude, destinationLongitude),
                origin = listOf(originLatitude, originLongitude)
            )
            assertTrue("Distance should be non-negative for coords: " +
                    "($destinationLatitude, $destinationLongitude) -> ($originLatitude, $originLongitude), " +
                    "got $distance", distance >= 0)
        }
    }

    /**
     * Verifies random out of range values always throws IllegalArgumentException
     */
    @Test
    fun haversine_throwsIllegalArgumentException_withRandomOutOfRange() {
        repeat(100) {
            val destinationLatitude = -90.1 - Random.nextDouble(0.0, 500.0)
            val destinationLongitude = -180.1 - Random.nextDouble(0.0, 800.0)
            val originLatitude = -90.1 - Random.nextDouble(0.0, 500.0)
            val originLongitude = -180.1 - Random.nextDouble(0.0, 800.0)

            try {
               AppUtils.haversine(
                    destination = Coordinate(destinationLatitude, destinationLongitude),
                    origin = listOf(originLatitude, originLongitude)
                )
                fail("Expected IllegalArgumentException to be thrown for:" +
                        "destinationLatitude = $destinationLatitude " +
                        "destinationLongitude = $destinationLongitude " +
                        "originLatitude = $originLatitude " +
                        "originLongitude = $originLongitude ")
            } catch(e: IllegalArgumentException) {
                assertTrue(e.message?.contains("out of range", ignoreCase = true) == true)
            }
        }
    }
}