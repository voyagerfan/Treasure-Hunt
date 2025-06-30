package com.example.treasurehunt

import androidx.compose.ui.graphics.evaluateCubic
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.example.treasurehunt.model.Coordinate
import com.example.treasurehunt.utils.AppUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows




class HaversineTest {

    private val testDestination = Coordinate(
        latitude = 54.1672,
        longitude = -4.4780
    )

    companion object {
        @JvmStatic
        fun invalidCoordinates() = listOf(
            listOf(-90.1, 0.0), // lat wrong
            listOf(0.0, -181.0), // lon wrong
            listOf(-91.0, 181.0), // both wrong
            // add more test cases here
        )
    }
    /*
    @ParameterizedTest
    @MethodSource("invalidCoordinates")
    fun `haversine throws exception on invalid coordinates`(coords: List<Double>) {
        val exception = assertThrows<IllegalArgumentException> {
            AppUtils.haversine(
                destination = testDestination,
                origin = coords,
            )
        }
        assertEquals("Latitude or longitude is out of range", exception.message)
    }*/
}