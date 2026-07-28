package com.marchenaya.runique

import com.marchenaya.core.presentation.ui.UiText

sealed interface MainEvent {
    data class ShowSnackbar(val message: UiText) : MainEvent
}
