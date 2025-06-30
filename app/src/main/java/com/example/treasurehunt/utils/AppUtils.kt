package com.example.treasurehunt.utils

import com.example.treasurehunt.model.Coordinate

object AppUtils {

    fun haversine(
        destination: Coordinate,
        origin: List<Double>
    ): Double {
        // origin[0] = origin latitude
        // origin[1] = origin longitude
        require(destination.latitude in -90.0..90.0) { "Latitude or Longitude value out of range" }
        require(destination.longitude in -180.0..180.0) { "Latitude or Longitude value of out range" }

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