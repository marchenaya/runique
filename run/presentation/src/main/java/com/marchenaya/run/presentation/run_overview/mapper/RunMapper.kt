package com.marchenaya.run.presentation.run_overview.mapper

import com.marchenaya.core.domain.run.Run
import com.marchenaya.core.presentation.ui.formatted
import com.marchenaya.core.presentation.ui.toFormattedKm
import com.marchenaya.core.presentation.ui.toFormattedKmH
import com.marchenaya.core.presentation.ui.toFormattedMeters
import com.marchenaya.core.presentation.ui.toFormattedPace
import com.marchenaya.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val distanceKm = distanceMeters / 1000.0

    return RunUi(
        id = requireNotNull(id) { "Run id must not be null when displayed in overview" },
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        averageSpeed = averageSpeedKmH.toFormattedKm(),
        maxSpeed = maxSpeedKmH.toFormattedKmH(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )
}