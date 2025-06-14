package com.example.treasurehunt.utils

import com.example.treasurehunt.model.Geo

object AppUtils {

    fun haversine(
        destination: Geo,
        origin: List<Double>
    ): Double {
        // origin[0] = origin latitude
        // origin[1] = origin longitude
        val earthRadiusKm = 6372.8

        val dLat = Math.toRadians(destination.dLat - origin[0])
        val dLon = Math.toRadians(destination.dLon - origin[1])
        val originLat = Math.toRadians(origin[0])
        val destinationLat = Math.toRadians(destination.dLat)
        val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(
            Math.sin(dLon / 2),
            2.toDouble()
        ) * Math.cos(originLat) * Math.cos(destinationLat)
        val c = 2 * Math.asin(Math.sqrt(a))
        return earthRadiusKm * c
    }
}