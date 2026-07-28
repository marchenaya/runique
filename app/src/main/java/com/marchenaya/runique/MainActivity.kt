package com.marchenaya.runique

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.marchenaya.core.presentation.designsystem.RuniqueTheme
import com.marchenaya.runique.components.RuniqueRoot
import org.koin.androidx.viewmodel.ext.android.viewModel

//todo make todos and check homeworks use to test dynamic feature : https://developer.android.com/guide/navigation/navigation-3/recipes/dynamicfeature#how-to-test-locally
class MainActivity : ComponentActivity() {

    private lateinit var splitInstallManager: SplitInstallManager
    private val splitInstallListener =
        SplitInstallStateUpdatedListener { state ->
            when (state.status()) {

                SplitInstallSessionStatus.INSTALLED -> {
                    viewModel.onAction(MainAction.OnAnalyticsFeatureInstalled)
                }

                SplitInstallSessionStatus.INSTALLING -> {
                    viewModel.onAction(MainAction.OnAnalyticsFeatureLoading)
                }

                SplitInstallSessionStatus.DOWNLOADING -> {
                    viewModel.onAction(MainAction.OnAnalyticsFeatureLoading)
                }

                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    splitInstallManager.startConfirmationDialogForResult(state, this, 0)
                }

                SplitInstallSessionStatus.FAILED -> {
                    viewModel.onAction(MainAction.OnAnalyticsFeatureInstallFailed)
                }

                else -> Unit
            }
        }

    private val viewModel by viewModel<MainViewModel>()

    private var currentIntent by mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentIntent = intent
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }
        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
        setContent {
            RuniqueTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!viewModel.state.isCheckingAuth) {
                        RuniqueRoot(
                            state = viewModel.state,
                            events = viewModel.events,
                            deepLinkUri = currentIntent?.data,
                            onDeepLinkHandled = { currentIntent = null },
                            onAnalyticsClick = ::installOrStartAnalyticsFeature
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        splitInstallManager.registerListener(splitInstallListener)
    }

    override fun onPause() {
        super.onPause()
        splitInstallManager.unregisterListener(splitInstallListener)
    }

    private fun installOrStartAnalyticsFeature() {
        if (splitInstallManager.installedModules.contains(ANALYTICS_FEATURE)) {
            Intent()
                .setClassName(
                    packageName,
                    ANALYTICS_ACTIVITY_CLASS
                )
                .also(::startActivity)
            return
        }

        val request = SplitInstallRequest.newBuilder()
            .addModule(ANALYTICS_FEATURE)
            .build()
        splitInstallManager
            .startInstall(request)
            .addOnFailureListener {
                it.printStackTrace()
                viewModel.onAction(MainAction.OnAnalyticsFeatureLoadFailed)
            }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntent = intent
    }

    private companion object {
        private const val ANALYTICS_FEATURE = "analytics_feature"
        private const val ANALYTICS_ACTIVITY_CLASS =
            "com.marchenaya.analytics.analytics_feature.AnalyticsActivity"
    }

}