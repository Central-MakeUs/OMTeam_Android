package com.omteam.impl.entry

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.AccountLinkCompleteNavKey
import com.omteam.api.LoginNavKey
import com.omteam.impl.LoginViewModel
import com.omteam.impl.screen.AccountLinkCompleteScreen
import com.omteam.impl.screen.LoginScreen
import timber.log.Timber

/**
 * 로그인 화면 Entry Builder
 *
 * @param onNavigateToAccountLinkComplete 계정 연동 완료 화면 이동
 */
fun EntryProviderScope<NavKey>.loginEntry(
    onNavigateToAccountLinkComplete: () -> Unit
) {
    entry<LoginNavKey> {
        val loginViewModel: LoginViewModel = hiltViewModel()
        val context = LocalContext.current

        LoginScreen(
            onKakaoLogin = { loginViewModel.loginWithKakao(context) },
            onGoogleLogin = { loginViewModel.loginWithGoogle(context) },
            onSignOut = { loginViewModel.logout() }
        )

        val loginState by loginViewModel.loginState.collectAsState()

        LaunchedEffect(loginState) {
            if (loginState is LoginViewModel.LoginState.Success) {
                onNavigateToAccountLinkComplete()
                loginViewModel.resetLoginState()
            }
        }
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
        Timber.e("## 계정 연동 완료 화면으로 이동")
        AccountLinkCompleteScreen {
            onNavigateToOnboarding()
        }
    }
}