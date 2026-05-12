package com.marchenaya.runique

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.marchenaya.auth.presentation.intro.IntroScreenRoot
import com.marchenaya.auth.presentation.register.RegisterScreenRoot

@Composable
fun NavigationRoot() {
    val navigationState = rememberNavigationState(
        startRoute = Routes.Intro,
        topLevelRoutes = setOf(Routes.Intro)
    )
    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        authGraph(navigator)
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}

private fun EntryProviderScope<NavKey>.authGraph(navigator: Navigator) {
    entry<Routes.Intro> {
        IntroScreenRoot(
            onSignInClick = {
                navigator.navigate(Routes.Login)
            },
            onSignUpClick = {
                navigator.navigate(Routes.Register)
            }
        )
    }
    entry<Routes.Register> {
        RegisterScreenRoot(
            onSignInClick = {
                navigator.navigate(
                    route = Routes.Login,
                    popUpTo = Routes.Register,
                    inclusive = true,
                    saveState = true,
                    restoreState = true
                )
            },
            onSuccessfulRegistration = {
                navigator.navigate(Routes.Login)
            }
        )
    }
}