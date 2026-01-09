package com.omteam.impl.entry

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.AccountLinkCompleteNavKey
import com.omteam.api.LoginNavKey
import com.omteam.impl.screen.AccountLinkCompleteScreen
import com.omteam.impl.screen.LoginScreen

/**
 * 로그인 화면 Entry Builder
 *
 * @param onNavigateToAccountLinkComplete 계정 연동 완료 화면 이동
 */
fun EntryProviderScope<NavKey>.loginEntry(
    onNavigateToAccountLinkComplete: () -> Unit
) {
    entry<LoginNavKey> {
        Log.e("test", "로그인 화면으로 이동")
        LoginScreen(
            onLoginSuccess = onNavigateToAccountLinkComplete
        )
    }
}

/**
 * 계정 연동 완료 화면 Entry Builder
 *
 * @param onNavigateToOnboarding 온보딩 화면으로 이동
 */
fun EntryProviderScope<NavKey>.accountLinkCompleteEntry(
    onNavigateToOnboarding: () -> Unit
) {
    entry<AccountLinkCompleteNavKey> {
        Log.e("test", "## 계정 연동 완료 화면으로 이동")
        AccountLinkCompleteScreen {
            onNavigateToOnboarding()
        }
    }
}