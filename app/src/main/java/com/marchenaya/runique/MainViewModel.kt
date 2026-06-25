package com.marchenaya.runique

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marchenaya.core.domain.SessionStorage
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = sessionStorage.observeAuthInfo().firstOrNull() != null
            )
            state = state.copy(isCheckingAuth = false)
        }
    }

}