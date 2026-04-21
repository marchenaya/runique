package com.marchenaya.runique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.marchenaya.core.presentation.designsystem.AnalyticsIcon
import com.marchenaya.core.presentation.designsystem.RuniqueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RuniqueTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Icon(
                        imageVector = AnalyticsIcon,
                        contentDescription = null,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}