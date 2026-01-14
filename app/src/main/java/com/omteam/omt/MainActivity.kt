package com.omteam.omt

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                // TODO : 온보딩 구현 후 아래 코드 원복해서 사용
//                val backStackState = remember { mutableStateOf<List<NavKey>>(listOf(LoginNavKey)) }
                val backStackState = remember { mutableStateOf<List<NavKey>>(listOf(OnboardingNavKey(step = 1))) }
                val navigator = remember { AppNavigator(backStackState) }

//                LaunchedEffect(backStackState.value) {
//                    Timber.e("## 백스택 : ${backStackState.value.map { it::class.simpleName }}")
//                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDisplay(
                        backStack = backStackState.value,
                        onBack = { navigator.popBackStack() },
                        entryProvider = entryProvider {
                            // TODO : 온보딩 구현 후 아래 코드 원복해서 사용
//                            loginEntry(
//                                onNavigateToAccountLinkComplete = {
//                                    navigator.navigateToAccountLinkComplete()
//                                }
//                            )
//
//                            accountLinkCompleteEntry(
//                                onNavigateToOnboarding = {
//                                    navigator.navigateToOnboarding()
//                                }
//                            )

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