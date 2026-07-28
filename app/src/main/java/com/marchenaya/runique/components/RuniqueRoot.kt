package com.marchenaya.runique.components

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.marchenaya.core.data.util.DefaultDispatcherProvider
import com.marchenaya.core.domain.util.DispatcherProvider
import com.marchenaya.core.presentation.designsystem.RuniqueTheme
import com.marchenaya.core.presentation.ui.snackbar.SnackbarProvider
import com.marchenaya.runique.MainState
import com.marchenaya.runique.navigation.NavigationRoot
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@Composable
fun RuniqueRoot(
    state: MainState,
    deepLinkUri: Uri?,
    onDeepLinkHandled: () -> Unit
) {
    SnackbarProvider {
        NavigationRoot(
            isLoggedIn = state.isLoggedIn,
            deepLinkUri = deepLinkUri,
            onDeepLinkHandled = onDeepLinkHandled
        )
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
                    deepLinkUri = null,
                    onDeepLinkHandled = {}
                )
            }
        })
}
