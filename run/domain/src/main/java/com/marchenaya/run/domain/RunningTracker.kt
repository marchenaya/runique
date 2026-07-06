package com.marchenaya.run.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn


class RunningTracker(
    private val locationObserver: LocationObserver,
    applicationScope: CoroutineScope
) {

    private val isObservingLocation = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentLocation = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if (isObservingLocation) {
                locationObserver.observerLocation(LOCATION_INTERVAL_MS)
            } else {
                flowOf()
            }
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            null
        )

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    companion object {
        private const val LOCATION_INTERVAL_MS = 1000L
    }

}