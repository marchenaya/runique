package com.marchenaya.runique.components

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.marchenaya.core.data.util.DefaultDispatcherProvider
import com.marchenaya.core.domain.util.DispatcherProvider
import com.marchenaya.core.presentation.designsystem.RuniqueTheme
import com.marchenaya.core.presentation.ui.ObserveAsEvents
import com.marchenaya.core.presentation.ui.snackbar.LocalSnackbar
import com.marchenaya.core.presentation.ui.snackbar.SnackbarProvider
import com.marchenaya.runique.MainEvent
import com.marchenaya.runique.MainState
import com.marchenaya.runique.navigation.NavigationRoot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@Composable
fun RuniqueRoot(
    state: MainState,
    events: Flow<MainEvent>,
    deepLinkUri: Uri?,
    onDeepLinkHandled: () -> Unit,
    onAnalyticsClick: () -> Unit,
) {
    SnackbarProvider {
        val snackbar = LocalSnackbar.current
        ObserveAsEvents(events) { event ->
            when (event) {
                is MainEvent.ShowSnackbar -> snackbar.show(event.message)
            }
        }

        NavigationRoot(
            isLoggedIn = state.isLoggedIn,
            deepLinkUri = deepLinkUri,
            onDeepLinkHandled = onDeepLinkHandled,
            onAnalyticsClick = onAnalyticsClick
        )

        if (state.showAnalyticsInstallDialog) {
            AnalyticsInstallDialog()
        }
    }
}

@Preview
@Composable
private fun RuniqueRootPreview() {
    KoinApplication(
        configuration = koinConfiguration(
            declaration = {
                modules(
                    module {
                        single<DispatcherProvider> { DefaultDispatcherProvider() }
                    }
                )
            }
        ),
        content = {
            RuniqueTheme {
                RuniqueRoot(
                    state = MainState(),
                    events = emptyFlow(),
                    deepLinkUri = null,
                    onDeepLinkHandled = {},
                    onAnalyticsClick = {}
                )
            }
        })
}

