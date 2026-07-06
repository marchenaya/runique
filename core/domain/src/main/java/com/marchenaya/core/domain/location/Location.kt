package com.marchenaya.core.domain.location

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Location(
    val latitude: Double,
    val longitude: Double
) {

    fun distanceTo(other: Location): Float {
        val latitudeDistance = Math.toRadians(other.latitude - latitude)
        val longitudeDistance = Math.toRadians(other.longitude - longitude)
        val haversine = sin(latitudeDistance / 2) * sin(latitudeDistance / 2) +
                cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude)) *
                sin(longitudeDistance / 2) * sin(longitudeDistance / 2)
        val angularDistance = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))

        return EARTH_RADIUS_METERS * angularDistance.toFloat()
    }

    companion object {
        private const val EARTH_RADIUS_METERS = 6_371_000
    }

}
