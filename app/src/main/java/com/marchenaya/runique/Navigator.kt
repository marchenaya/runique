package com.marchenaya.runique

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 */
class Navigator(val state: NavigationState) {

    fun navigate(
        route: NavKey,
        popUpTo: NavKey? = null,
        inclusive: Boolean = false,
        saveState: Boolean = false,
        restoreState: Boolean = false
    ) {
        if (route in state.backStacks.keys) {
            // This is a top level route, just switch to it.
            state.topLevelRoute = route
        } else {
            val currentStack = state.backStacks[state.topLevelRoute]
            if (currentStack != null) {
                if (popUpTo != null) {
                    // Find the index of the route to pop up to
                    val index = currentStack.indexOfLast { it == popUpTo }
                    if (index != -1) {
                        val popCount = if (inclusive) {
                            currentStack.size - index
                        } else {
                            currentStack.size - index - 1
                        }
                        repeat(popCount) {
                            currentStack.removeLastOrNull()
                        }
                    }
                }

                // Navigation 3 NavBackStack handles state saving/restoration for keys
                // by default if using rememberNavBackStack and decorators.
                // For simplicity in this migration, we add the new route.
                currentStack.add(route)
            }
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.lastOrNull()

        // If we're at the base of the current route, go back to the start route stack.
        if ((currentRoute == state.topLevelRoute) || (currentRoute == null)) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
