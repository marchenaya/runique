package com.marchenaya.runique.navigation.dynamicfeature

import com.google.android.play.core.splitinstall.SplitInstallSessionState

sealed interface InstallStatus {
    data object Idle : InstallStatus
    data class Pending(val sessionId: Int) : InstallStatus
    data class Downloading(val sessionId: Int, val progress: Float) : InstallStatus
    data class Installing(val sessionId: Int) : InstallStatus
    data class RequiresUserConfirmation(val state: SplitInstallSessionState) : InstallStatus
    data class Failed(val sessionId: Int, val errorCode: Int) : InstallStatus
}