package com.marchenaya.runique

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.marchenaya.auth.presentation.intro.IntroScreenRoot
import com.marchenaya.auth.presentation.login.LoginScreenRoot
import com.marchenaya.auth.presentation.register.RegisterScreenRoot
import com.marchenaya.core.domain.util.URL_ACTIVE_RUN
import com.marchenaya.run.presentation.active_run.ActiveRunScreenRoot
import com.marchenaya.run.presentation.active_run.service.ActiveRunService
import com.marchenaya.run.presentation.run_overview.RunOverviewScreenRoot

private val deepLinks: Map<String, NavKey> = mapOf(
    URL_ACTIVE_RUN to Routes.ActiveRun
)

@Composable
fun NavigationRoot(
    isLoggedIn: Boolean,
    deepLinkUri: Uri?,
    onDeepLinkHandled: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    val navigationState = rememberNavigationState(
        startRoute = if (isLoggedIn) Routes.RunOverview else Routes.Intro,
        topLevelRoutes = setOf(Routes.Intro, Routes.RunOverview)
    )
    val navigator = remember { Navigator(navigationState) }

    LaunchedEffect(deepLinkUri) {
        val route = deepLinkUri?.let { deepLinks[it.toString()] } ?: return@LaunchedEffect
        navigator.navigate(route)
        onDeepLinkHandled()
    }

    val entryProvider = entryProvider {
        authGraph(navigator)
        runGraph(navigator, onAnalyticsClick)
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
    entry<Routes.Login> {
        LoginScreenRoot(
            onLoginSuccess = {
                navigator.navigate(
                    route = Routes.RunOverview,
                    popUpTo = Routes.Intro,
                    inclusive = true
                )
            },
            onSignUpClick = {
                navigator.navigate(
                    route = Routes.Register,
                    popUpTo = Routes.Login,
                    inclusive = true,
                    saveState = true,
                    restoreState = true
                )
            }
        )
    }
}

private fun EntryProviderScope<NavKey>.runGraph(
    navigator: Navigator,
    onAnalyticsClick: () -> Unit
) {
    entry<Routes.RunOverview> {
        RunOverviewScreenRoot(
            onStartRunClick = {
                navigator.navigate(
                    Routes.ActiveRun
                )
            },
            onLogoutClick = {
                navigator.navigate(
                    route = Routes.Intro,
                    popUpTo = Routes.RunOverview,
                    inclusive = true
                )
            }
        )
    }
    entry<Routes.ActiveRun> {
        val context = LocalContext.current
        ActiveRunScreenRoot(
            onBack = {
                navigator.goBack()
            },
            onFinish = {
                navigator.goBack()
            },
            onServiceToggle = { shouldServiceRun ->
                if (shouldServiceRun) {
                    context.startService(
                        ActiveRunService.createStartIntent(
                            context = context,
                            activityClass = MainActivity::class.java
                        )
                    )
                } else {
                    context.startService(
                        ActiveRunService.createStopIntent(context = context)
                    )
                }
            }
        )
    }
}