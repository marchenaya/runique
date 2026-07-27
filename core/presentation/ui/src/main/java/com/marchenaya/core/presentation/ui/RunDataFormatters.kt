package com.marchenaya.core.presentation.ui

import java.util.Locale
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

fun Duration.formatted(): UiText {
    val totalSeconds = inWholeSeconds
    val hours = String.format(Locale.getDefault(), "%02d", totalSeconds / 3600)
    val minutes = String.format(Locale.getDefault(), "%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format(Locale.getDefault(), "%02d", totalSeconds % 60)

    return UiText.StringResource(R.string.format_duration, hours, minutes, seconds)
}

fun Double.toFormattedKm(): UiText {
    return UiText.StringResource(R.string.format_distance_km, roundToDecimals(1))
}

fun Duration.toFormattedPace(distanceKm: Double): UiText {
    if (this == Duration.ZERO || distanceKm <= 0.0) {
        return UiText.StringResource(R.string.format_no_value)
    }

    val secondsPerKm = (this.inWholeSeconds / distanceKm).roundToInt()
    val averagePaceMinutes = secondsPerKm / 60
    val averagePaceSeconds = String.format(Locale.getDefault(), "%02d", secondsPerKm % 60)

    return UiText.StringResource(
        R.string.format_pace_per_km,
        "$averagePaceMinutes:$averagePaceSeconds"
    )
}

fun Double.toFormattedKmH(): UiText {
    return UiText.StringResource(R.string.format_speed_kmh, roundToDecimals(1))
}

fun Int.toFormattedMeters(): UiText {
    return UiText.StringResource(R.string.format_elevation_meters, this)
}

private fun Double.roundToDecimals(decimalCount: Int): Double {
    val factor = 10f.pow(decimalCount)
    return round(this * factor) / factor
}