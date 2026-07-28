package com.marchenaya.runique.navigation.dynamicfeature

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.RetainObserver
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

@Stable
class DynamicFeatureManager(context: Context) : RetainObserver {

    private val splitInstallManager = SplitInstallManagerFactory.create(context)

    private var activeSessionId: Int? = null
    private var activeModuleName: String? = null
    private var onModuleInstalledCallback: (() -> Unit)? = null

    var status by mutableStateOf<InstallStatus>(InstallStatus.Idle)
        private set

    var installedModules: Set<String> by mutableStateOf(splitInstallManager.installedModules.toSet())
        private set

    private val listener = SplitInstallStateUpdatedListener { state ->
        if (activeSessionId == null && activeModuleName != null &&
            state.moduleNames().contains(activeModuleName)
        ) {
            activeSessionId = state.sessionId()
        }
        if (state.sessionId() == activeSessionId) {
            updateStatus(state)
        }
    }

    override fun onRetained() {
        splitInstallManager.registerListener(listener)
    }

    override fun onRetired() {
        splitInstallManager.unregisterListener(listener)
    }

    override fun onEnteredComposition() = Unit
    override fun onExitedComposition() = Unit
    override fun onUnused() = Unit

    fun installModule(moduleName: String, onModuleInstalled: () -> Unit) {
        if (splitInstallManager.installedModules.contains(moduleName)) {
            onModuleInstalled()
            return
        }
        if (status !is InstallStatus.Idle && status !is InstallStatus.Failed) return

        activeModuleName = moduleName
        onModuleInstalledCallback = onModuleInstalled
        status = InstallStatus.Pending(UNKNOWN_SESSION_ID)

        splitInstallManager
            .startInstall(
                SplitInstallRequest.newBuilder().addModule(moduleName).build()
            )
            .addOnSuccessListener { sessionId ->
                if (activeSessionId == null) activeSessionId = sessionId
                val current = status
                if (current is InstallStatus.Pending && current.sessionId == UNKNOWN_SESSION_ID) {
                    status = InstallStatus.Pending(sessionId)
                }
            }
            .addOnFailureListener {
                status = InstallStatus.Failed(UNKNOWN_SESSION_ID, UNKNOWN_ERROR_CODE)
                clearSessionState()
            }
    }

    fun startConfirmationDialogForResult(
        state: SplitInstallSessionState,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        splitInstallManager.startConfirmationDialogForResult(state, launcher)
    }

    fun cancelInstallModule() {
        activeSessionId?.let(splitInstallManager::cancelInstall)
        status = InstallStatus.Idle
        clearSessionState()
    }

    private fun updateStatus(state: SplitInstallSessionState) {
        val sessionId = state.sessionId()
        when (state.status()) {
            SplitInstallSessionStatus.PENDING -> {
                status = InstallStatus.Pending(sessionId)
            }

            SplitInstallSessionStatus.DOWNLOADING -> {
                val total = state.totalBytesToDownload()
                val progress = if (total > 0) state.bytesDownloaded().toFloat() / total else 0f
                status = InstallStatus.Downloading(sessionId, progress)
            }

            SplitInstallSessionStatus.DOWNLOADED,
            SplitInstallSessionStatus.INSTALLING -> {
                status = InstallStatus.Installing(sessionId)
            }

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                status = InstallStatus.RequiresUserConfirmation(state)
            }

            SplitInstallSessionStatus.INSTALLED -> {
                installedModules = splitInstallManager.installedModules.toSet()
                val callback = onModuleInstalledCallback
                status = InstallStatus.Idle
                clearSessionState()
                callback?.invoke()
            }

            SplitInstallSessionStatus.FAILED -> {
                status = InstallStatus.Failed(sessionId, state.errorCode())
                clearSessionState()
            }

            SplitInstallSessionStatus.CANCELING -> {
                status = InstallStatus.Pending(sessionId)
            }

            SplitInstallSessionStatus.CANCELED -> {
                status = InstallStatus.Idle
                clearSessionState()
            }

            SplitInstallSessionStatus.UNKNOWN -> {
                status = InstallStatus.Failed(sessionId, UNKNOWN_ERROR_CODE)
                clearSessionState()
            }
        }
    }

    private fun clearSessionState() {
        activeSessionId = null
        activeModuleName = null
        onModuleInstalledCallback = null
    }

    private companion object {
        const val UNKNOWN_SESSION_ID = -1
        const val UNKNOWN_ERROR_CODE = -1
    }
}

@Composable
fun retainDynamicFeatureManager(): DynamicFeatureManager {
    val applicationContext = LocalContext.current.applicationContext
    return retain { DynamicFeatureManager(applicationContext) }
}
