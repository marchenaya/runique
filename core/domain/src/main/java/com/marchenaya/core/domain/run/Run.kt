package com.marchenaya.core.domain.run

import com.marchenaya.core.domain.location.Location
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    val id: String?, //null if new run
    val duration: Duration,
    val dateTimeUtc: ZonedDateTime,
    val distanceMeters: Int,
    val location: Location, //TODO : use places Google API to retrieve run location name
    val maxSpeedKmH: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
) {
    val averageSpeedKmH: Double
        get() = (distanceMeters / 1000.0) / duration.toDouble(DurationUnit.HOURS)
}
