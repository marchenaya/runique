package com.marchenaya.runique

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marchenaya.core.domain.SessionStorage
import com.marchenaya.core.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = sessionStorage.observeAuthInfo().firstOrNull() != null
            )
            state = state.copy(isCheckingAuth = false)
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            MainAction.OnAnalyticsFeatureLoading -> {
                setAnalyticsDialogVisibility(true)
            }

            MainAction.OnAnalyticsFeatureInstalled -> {
                setAnalyticsDialogVisibility(false)
                sendSnackbar(UiText.StringResource(R.string.analytics_installed))
            }

            MainAction.OnAnalyticsFeatureInstallFailed -> {
                setAnalyticsDialogVisibility(false)
                sendSnackbar(UiText.StringResource(R.string.error_installation_failed))
            }

            MainAction.OnAnalyticsFeatureLoadFailed -> {
                sendSnackbar(UiText.StringResource(R.string.error_couldnt_load_module))
            }
        }
    }

    private fun setAnalyticsDialogVisibility(isVisible: Boolean) {
        state = state.copy(showAnalyticsInstallDialog = isVisible)
    }

    private fun sendSnackbar(message: UiText) {
        viewModelScope.launch {
            eventChannel.send(MainEvent.ShowSnackbar(message))
        }
    }

}