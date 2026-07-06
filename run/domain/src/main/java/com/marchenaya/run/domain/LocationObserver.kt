package com.marchenaya.run.domain

import com.marchenaya.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun observerLocation(interval: Long): Flow<LocationWithAltitude>
}