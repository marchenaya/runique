package com.marchenaya.runique

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.marchenaya.auth.presentation.intro.IntroScreenRoot
import com.marchenaya.auth.presentation.register.RegisterScreenRoot

//TODO : Migrate to last navigation
@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Auth
    ) {
        authGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Routes.Auth>(
        startDestination = Routes.Intro
    ) {
        composable<Routes.Intro> {
            IntroScreenRoot(
                onSignInClick = {
                    navController.navigate(Routes.Register)
                },
                onSignUpClick = {
                    navController.navigate(Routes.Login)
                }
            )
        }
        composable<Routes.Register> {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Register) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(Routes.Login)
                }
            )
        }
    }
}