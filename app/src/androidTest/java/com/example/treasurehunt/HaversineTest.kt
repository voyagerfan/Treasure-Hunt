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
import org.junit.Assert.*


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

@RunWith(Parameterized::class)
class HaversineAccuracyParameterizedTest(
    private val destination: Coordinate,
    private val origin: List<Double>,
    private val expectedDistance: Double
){
    /**
     * Verifies that the haversine function accuracy at different scales to
     * within +/- 0.03% error of the expected value.
     */
    @Test
    fun calculateDistance_isWithinKnownKilometerRange() {
        val distance = AppUtils.haversine(destination, origin)
        val percentError = 0.0003 // 0.03% error
        val distanceLowerLimit = expectedDistance - (percentError * expectedDistance)
        val distanceUpperLimit = expectedDistance + (percentError * expectedDistance)
        assertTrue(
            "Incorrect distance, expected $expectedDistance but found $distance",
            distance in distanceLowerLimit..distanceUpperLimit
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf(Coordinate(41.8902, 12.4922), listOf(41.9029, 12.4534), 3.508),
            arrayOf(Coordinate(37.7749, 122.4194), listOf(37.3382, 121.8863), 67.574),
            arrayOf(Coordinate(48.8566, 2.3522), listOf(52.3676, 4.9041), 429.861),
            arrayOf(Coordinate(40.7128, 74.0060), listOf(51.5074, 0.1278), 5570.222)
        )
    }
}

@RunWith(Parameterized::class)
class HaversineSymmetryParameterizedTest(
    private val destination: Coordinate,
    private val origin: List<Double>,
){
    /**
     * Verifies that the haversine function is symmetric - swapping the destination
     * and origin data will produce the same result within a tolerance of 0.0001.
     */
    private fun coordinateToDoubleList(coordinate: Coordinate): List<Double> {
        return listOf(coordinate.latitude, coordinate.longitude)
    }

    private fun doubleListToCoordinate(list: List<Double>): Coordinate {
        return Coordinate(latitude = list[0], longitude = list[1])
    }

    @Test
    fun calculateForwardAndReverseDistance_isSymmetric() {
        val forwardDistance = AppUtils.haversine(destination, origin)
        val reverseDistance = AppUtils.haversine(doubleListToCoordinate(origin), coordinateToDoubleList(destination))
        val tolerance = 0.0001
        assertEquals(
            "Symmetry does not hold for $destination and $origin",
            forwardDistance,
            reverseDistance,
            tolerance
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf(Coordinate(41.8902, 12.4922), listOf(41.9029, 12.4534)),
            arrayOf(Coordinate(37.7749, 122.4194), listOf(37.3382, 121.8863)),
            arrayOf(Coordinate(48.8566, 2.3522), listOf(52.3676, 4.9041)),
            arrayOf(Coordinate(40.7128, 74.0060), listOf(51.5074, 0.1278))
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