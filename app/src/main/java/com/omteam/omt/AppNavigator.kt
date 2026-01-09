package com.omteam.omt

import androidx.compose.runtime.MutableState
import androidx.navigation3.runtime.NavKey
import com.omteam.api.AccountLinkCompleteNavKey
import com.omteam.api.LoginNavKey

class AppNavigator(
    private val backStackState: MutableState<List<NavKey>>
) {
    private val backStack: List<NavKey>
        get() = backStackState.value

    fun navigate(destination: NavKey, popUpTo: NavKey? = null, inclusive: Boolean = false) {
        backStackState.value = if (popUpTo != null) {
            val index = backStack.indexOfLast { it == popUpTo }
            val newStack = if (index >= 0) {
                if (inclusive) {
                    backStack.subList(0, index)
                } else {
                    backStack.subList(0, index + 1)
                }
            } else {
                backStack
            }
            newStack + destination
        } else {
            backStack + destination
        }
    }

    fun popBackStack() {
        if (backStack.size > 1) {
            backStackState.value = backStack.dropLast(1)
        }
    }

    fun navigateToAccountLinkComplete() =
        navigate(
            AccountLinkCompleteNavKey,
            popUpTo = LoginNavKey,
            inclusive = false
        )

    fun navigateToOnboarding() {
        // TODO : 온보딩 화면 구현 후 navigate() 추가
    }
}