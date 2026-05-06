package com.marchenaya.runique

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {

    @Serializable
    data object Intro : Routes

    @Serializable
    data object Register : Routes

    @Serializable
    data object Login : Routes

}