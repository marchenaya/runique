package com.marchenaya.runique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.marchenaya.core.presentation.designsystem.RuniqueTheme
import com.marchenaya.core.presentation.ui.SnackbarScaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RuniqueTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SnackbarScaffold {
                        NavigationRoot(
                            isLoggedIn = false, // TODO: Get this from a ViewModel later
                            onAnalyticsClick = {}
                        )
                    }
                }
            }
        }
    }
}
