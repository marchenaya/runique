package com.marchenaya.run.presentation.active_run.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.marchenaya.core.domain.location.LocationTimestamp
import com.marchenaya.core.presentation.designsystem.RuniqueTheme

@Composable
fun RuniquePolylines(
    locations: List<List<LocationTimestamp>>
) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { firstTimestamp, secondTimestamp ->
                PolylineUi(
                    firstLocation = firstTimestamp.location.location,
                    secondLocation = secondTimestamp.location.location,
                    color = PolylineColorCalculator.locationsToColor(
                        firstLocation = firstTimestamp,
                        secondLocation = secondTimestamp
                    )
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.firstLocation.latitude, polylineUi.firstLocation.longitude),
                    LatLng(polylineUi.secondLocation.latitude, polylineUi.secondLocation.longitude)
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }
}

@Preview
@Composable
private fun RuniquePolylinesPreview() {
    RuniqueTheme {
        RuniquePolylines(
            locations = listOf()
        )
    }
}