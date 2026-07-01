package com.marchenaya.core.presentation.ui.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SnackbarProvider(
    content: @Composable () -> Unit
) {
    val viewModel: SnackbarViewModel = viewModel()
    val context = LocalContext.current

    val controller = remember(viewModel, context) {
        SnackbarController(
            hostState = viewModel.hostState,
            show = { uiText -> viewModel.show(uiText.asString(context)) }
        )
    }

    CompositionLocalProvider(LocalSnackbar provides controller) {
        content()
    }
}