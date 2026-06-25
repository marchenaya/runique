package com.marchenaya.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun SnackbarScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    CompositionLocalProvider(
        LocalShowSnackbar provides { uiText ->
            scope.launch { snackbarHostState.showSnackbar(uiText.asString(context)) }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Box(modifier = Modifier.consumeWindowInsets(innerPadding)) {
                content()
            }
        }
    }
}
