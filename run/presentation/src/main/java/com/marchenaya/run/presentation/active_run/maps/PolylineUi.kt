package com.marchenaya.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import com.marchenaya.core.domain.location.Location

data class PolylineUi(
    val firstLocation: Location,
    val secondLocation: Location,
    val color: Color
)
