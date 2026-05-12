package com.marchenaya.auth.presentation.register

import com.marchenaya.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess : RegisterEvent
    data class ShowSnackbar(val message: UiText) : RegisterEvent
}