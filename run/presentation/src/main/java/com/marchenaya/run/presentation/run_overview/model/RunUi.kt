package com.marchenaya.run.presentation.run_overview.model

import com.marchenaya.core.presentation.ui.UiText

data class RunUi(
    val id: String,
    val duration: String,
    val dateTime: String,
    val distance: UiText,
    val averageSpeed: UiText,
    val maxSpeed: UiText,
    val pace: UiText,
    val totalElevation: UiText,
    val mapPictureUrl: String?
)