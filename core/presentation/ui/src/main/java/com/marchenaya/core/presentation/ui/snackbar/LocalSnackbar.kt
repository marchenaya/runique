package com.marchenaya.core.presentation.ui.snackbar

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbar = staticCompositionLocalOf<SnackbarController> {
    error("No SnackbarController provided. Wrap content in SnackbarProvider.")
}