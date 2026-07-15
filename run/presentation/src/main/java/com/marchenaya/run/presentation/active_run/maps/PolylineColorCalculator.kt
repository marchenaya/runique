package com.marchenaya.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.marchenaya.core.domain.location.LocationTimestamp
import kotlin.math.abs

object PolylineColorCalculator {

    fun locationsToColor(
        firstLocation: LocationTimestamp,
        secondLocation: LocationTimestamp
    ): Color {
        val distanceMeters =
            firstLocation.location.location.distanceTo(secondLocation.location.location)
        val timeDiff =
            abs((secondLocation.durationTimestamp - firstLocation.durationTimestamp).inWholeSeconds)
        val speedKmH = (distanceMeters / timeDiff) * 3.6

        return interpolateColor(
            speedKmH = speedKmH,
            minSpeed = 5.0,
            maxSpeed = 20.0,
            colorStart = Color.Green,
            colorMid = Color.Yellow,
            colorEnd = Color.Red
        )
    }

    private fun interpolateColor(
        speedKmH: Double,
        minSpeed: Double,
        maxSpeed: Double,
        colorStart: Color,
        colorMid: Color,
        colorEnd: Color
    ): Color {
        val ratio = ((speedKmH - minSpeed) / (maxSpeed - minSpeed)).coerceIn(.0..1.0)
        val colorInt = if (ratio <= .5) {
            val startToMidRatio = ratio / .5
            ColorUtils.blendARGB(colorStart.toArgb(), colorMid.toArgb(), startToMidRatio.toFloat())
        } else {
            val midToEndRatio = (ratio - .5) / .5
            ColorUtils.blendARGB(colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat())
        }

        return Color(colorInt)
    }

}