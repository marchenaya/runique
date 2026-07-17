package com.marchenaya.core.database.mapper

import com.marchenaya.core.database.entity.RunEntity
import com.marchenaya.core.domain.location.Location
import com.marchenaya.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")), //todo : check if better to change to kotlin.time
        distanceMeters = distanceMeters,
        location = Location(
            latitude = latitude,
            longitude = longitude
        ),
        maxSpeedKmH = maxSpeedKmH,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        durationMillis = duration.inWholeMilliseconds,
        distanceMeters = distanceMeters,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        latitude = location.latitude,
        longitude = location.longitude,
        averageSpeedKmH = averageSpeedKmH,
        maxSpeedKmH = maxSpeedKmH,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}