package com.marchenaya.run.location

import android.location.Location
import com.marchenaya.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = com.marchenaya.core.domain.location.Location(
            latitude = latitude,
            longitude = longitude
        ),
        altitude = altitude
    )
}