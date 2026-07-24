package com.marchenaya.runique.navigation

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
            if (popUpTo != null && popUpTo in state.backStacks.keys && inclusive) {
                state.backStacks[popUpTo]?.clear()
                state.savedStacks[popUpTo]?.clear()
            }

            val targetStack = state.backStacks[route]
            if (targetStack != null && targetStack.isEmpty()) {
                targetStack.add(route)
            }

            state.topLevelRoute = route
        } else {
            val currentStack = state.backStacks[state.topLevelRoute]
            val savedStack = state.savedStacks[state.topLevelRoute]
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
                            val removed = currentStack.removeLastOrNull()
                            if (saveState && removed != null && savedStack != null &&
                                removed !in savedStack
                            ) {
                                savedStack.add(removed)
                            }
                        }
                    }
                }

                // If the target was previously parked, un-park it: it stayed decorated,
                // so re-adding the same key reuses its retained ViewModel/saveable state.
                if (restoreState && savedStack != null && route in savedStack) {
                    savedStack.remove(route)
                }
                currentStack.add(route)
            }
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return
        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        }
    }
}