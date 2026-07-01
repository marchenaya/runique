package com.marchenaya.core.presentation.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

class SnackbarViewModel : ViewModel() {

    val hostState = SnackbarHostState()

    private var job: Job? = null

    fun show(message: String, durationMillis: Long = SHORT_DURATION_MILLIS) {
        job?.cancel()
        job = viewModelScope.launch {
            withTimeoutOrNull(durationMillis.milliseconds) {
                hostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
            }
        }
    }

    private companion object {
        const val SHORT_DURATION_MILLIS = 4_000L
    }
}
