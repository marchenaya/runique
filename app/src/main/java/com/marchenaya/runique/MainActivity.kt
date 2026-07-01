package com.marchenaya.runique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.marchenaya.core.presentation.designsystem.RuniqueTheme
import com.marchenaya.core.presentation.ui.snackbar.SnackbarProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }
        setContent {
            RuniqueTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!viewModel.state.isCheckingAuth) {
                        SnackbarProvider {
                            NavigationRoot(
                                isLoggedIn = viewModel.state.isLoggedIn,
                                onAnalyticsClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}
