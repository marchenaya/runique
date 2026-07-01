package com.marchenaya.core.presentation.ui.snackbar

import androidx.compose.material3.SnackbarHostState
import com.marchenaya.core.presentation.ui.UiText

class SnackbarController(
    val hostState: SnackbarHostState,
    val show: (UiText) -> Unit,
)