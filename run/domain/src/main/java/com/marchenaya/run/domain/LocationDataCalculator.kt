package com.marchenaya.run.domain

import com.marchenaya.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {

    fun getTotalDistanceMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext { firstLocation, secondLocation ->
                    firstLocation.location.location.distanceTo(secondLocation.location.location)
                }.sum().roundToInt()
            }
    }

    fun getMaxSpeedKmH(locations: List<List<LocationTimestamp>>): Double {
        return locations.maxOf { locationSet ->
            locationSet.zipWithNext { firstLocation, secondLocation ->
                val distance = firstLocation.location.location.distanceTo(
                    other = secondLocation.location.location
                )
                val hoursDifference =
                    (secondLocation.durationTimestamp - firstLocation.durationTimestamp)
                        .toDouble(DurationUnit.HOURS)

                if (hoursDifference == .0) {
                    .0
                } else {
                    (distance / 1000.0) / hoursDifference
                }
            }.maxOrNull() ?: .0
        }
    }

    fun getTotalElevationMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations.sumOf { locationSet ->
            locationSet.zipWithNext { firstLocation, secondLocation ->
                val firstAltitude = firstLocation.location.altitude
                val secondAltitude = secondLocation.location.altitude
                (secondAltitude - firstAltitude).coerceAtLeast(.0)
            }.sum().roundToInt()
        }
    }

}