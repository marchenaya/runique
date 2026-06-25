package com.marchenaya.auth.presentation.login

import com.marchenaya.core.presentation.ui.UiText

sealed interface LoginEvent {
    data class ShowSnackbar(val message: UiText) : LoginEvent
    data object LoginSuccess : LoginEvent
}