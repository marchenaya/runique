package com.marchenaya.run.domain

import com.marchenaya.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt

object LocationDataCalculator {

    fun getTotalDistanceMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext { firstLocation, secondLocation ->
                    firstLocation.location.location.distanceTo(secondLocation.location.location)
                }.sum().roundToInt()
            }
    }

}