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
 * @param onNavigateToAccountLinkComplete 계정 연동 완료 화면 이동 (신규 사용자 - 온보딩 미완료)
 * @param onNavigateToMain 메인 화면 이동 (기존 사용자 - 온보딩 완료)
 */
fun EntryProviderScope<NavKey>.loginEntry(
    onNavigateToAccountLinkComplete: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    entry<LoginNavKey> {
        val loginViewModel: LoginViewModel = hiltViewModel()
        val context = LocalContext.current

        LoginScreen(
            onKakaoLogin = { loginViewModel.loginWithKakao(context) },
            onGoogleLogin = { loginViewModel.loginWithGoogle(context) }
        )

        val loginState by loginViewModel.loginState.collectAsState()

        LaunchedEffect(loginState) {
            if (loginState is LoginViewModel.LoginState.Success) {
                val success = loginState as LoginViewModel.LoginState.Success
                val loginResult = success.loginResult
                val userInfo = success.userInfo
                
                Timber.d("## 로그인 성공 - 온보딩 완료 : ${loginResult.onboardingCompleted}, 유저 : $userInfo")
                
                if (loginResult.onboardingCompleted) {
                    // 온보딩을 이미 완료한 기존 유저 → 메인 화면 바로 이동
                    onNavigateToMain()
                } else {
                    // 온보딩이 필요한 신규 유저 → 계정 연동 완료 화면 → 온보딩 화면
                    onNavigateToAccountLinkComplete()
                }
                
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