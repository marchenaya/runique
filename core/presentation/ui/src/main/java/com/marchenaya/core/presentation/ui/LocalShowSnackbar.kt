package com.marchenaya.core.presentation.ui

import androidx.compose.runtime.staticCompositionLocalOf

val LocalShowSnackbar = staticCompositionLocalOf<(UiText) -> Unit> {
    error("No snackbar handler provided. Wrap content in CompositionLocalProvider(LocalShowSnackbar provides ...).")
}