package com.marchenaya.core.domain

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object Timer {

    private const val EMIT_INTERVAL_MS = 200L

    fun timeAndEmit(): Flow<Duration> {
        return flow {
            var lastEmitTime = System.currentTimeMillis()
            while (currentCoroutineContext().isActive) {
                delay(EMIT_INTERVAL_MS.milliseconds)
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastEmitTime
                emit(elapsedTime.milliseconds)
                lastEmitTime = currentTime
            }
        }
    }
}