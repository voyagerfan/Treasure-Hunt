package com.example.treasurehunt.utils

import com.example.treasurehunt.model.Coordinate

object AppUtils {

    fun haversine(
        destination: Coordinate,
        origin: List<Double>
    ): Double {
        // origin[0] = origin latitude
        // origin[1] = origin longitude
        val earthRadiusKm = 6372.8

        val dLat = Math.toRadians(destination.latitude - origin[0])
        val dLon = Math.toRadians(destination.longitude - origin[1])
        val originLat = Math.toRadians(origin[0])
        val destinationLat = Math.toRadians(destination.latitude)
        val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(
            Math.sin(dLon / 2),
            2.toDouble()
        ) * Math.cos(originLat) * Math.cos(destinationLat)
        val c = 2 * Math.asin(Math.sqrt(a))
        return Math.round(earthRadiusKm * c * 1000).toDouble() / 1000
    }
}