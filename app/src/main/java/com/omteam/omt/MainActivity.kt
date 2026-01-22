package com.omteam.omt

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.omteam.api.LoginNavKey
import com.omteam.api.MainNavKey
import com.omteam.api.OnboardingNavKey
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.impl.entry.accountLinkCompleteEntry
import com.omteam.impl.entry.loginEntry
import com.omteam.impl.entry.mainEntry
import com.omteam.impl.entry.onboardingEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            // 라이트 모드일 때 상단 네비게이션 바 글자들을 검은색으로 표시
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            )
        )
        setContent {
            OMTeamTheme {
                val autoLoginState by viewModel.autoLoginState.collectAsStateWithLifecycle()
                
                // 자동 로그인 상태 따라 초기 화면 결정
                val initialNavKey = when (autoLoginState) {
                    MainViewModel.AutoLoginState.Checking -> null // TODO : 로딩 화면은 뭘로 보여주나?
                    MainViewModel.AutoLoginState.NeedLogin -> LoginNavKey
                    MainViewModel.AutoLoginState.NeedOnboarding -> OnboardingNavKey(step = 1)
                    MainViewModel.AutoLoginState.NavigateToMain -> MainNavKey
                }
                
                // 로딩 중이면 로딩 화면 표시
                if (initialNavKey == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    return@OMTeamTheme
                }
                
                val backStackState = remember { mutableStateOf(listOf(initialNavKey)) }
                val navigator = remember { AppNavigator(backStackState) }

                // 자동 로그인 완료 후 초기 화면 설정
                LaunchedEffect(initialNavKey) {
                    backStackState.value = listOf(initialNavKey)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDisplay(
                        backStack = backStackState.value,
                        onBack = { navigator.popBackStack() },
                        entryProvider = entryProvider {
                            loginEntry(
                                onNavigateToAccountLinkComplete = {
                                    navigator.navigateToAccountLinkComplete()
                                },
                                onNavigateToMain = {
                                    navigator.navigateToMain()
                                }
                            )

                            accountLinkCompleteEntry(
                                onNavigateToOnboarding = {
                                    navigator.navigateToOnboarding()
                                }
                            )

                            onboardingEntry(
                                onNavigateToNextStep = { currentStep ->
                                    backStackState.value += OnboardingNavKey(
                                        step = currentStep + 1
                                    )
                                },
                                onNavigateToMain = {
                                    // 온보딩 완료 후 메인 화면 이동
                                    backStackState.value = listOf(MainNavKey)
                                },
                                onNavigateBack = {
                                    navigator.popBackStack()
                                }
                            )

                            mainEntry()
                        },
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
    }
}