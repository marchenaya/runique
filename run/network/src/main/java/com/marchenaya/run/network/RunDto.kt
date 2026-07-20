package com.marchenaya.run.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RunDto(
    val id: String,
    val dateTimeUtc: String,
    val durationMillis: Long,
    val distanceMeters: Int,
    @SerialName("lat")
    val latitude: Double,
    @SerialName("long")
    val longitude: Double,
    @SerialName("avgSpeedKmh")
    val averageSpeedKmH: Double,
    @SerialName("maxSpeedKmh")
    val maxSpeedKmH: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
)
