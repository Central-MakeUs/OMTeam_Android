package com.omteam.omt

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.omteam.api.LoginNavKey
import com.omteam.api.MainNavKey
import com.omteam.api.OnboardingNavKey
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.impl.entry.accountLinkCompleteEntry
import com.omteam.impl.entry.editExerciseTimeEntry
import com.omteam.impl.entry.editFavoriteExerciseEntry
import com.omteam.impl.entry.editMissionTimeEntry
import com.omteam.impl.entry.editMyGoalEntry
import com.omteam.impl.entry.editMyInfoEntry
import com.omteam.impl.entry.editPatternEntry
import com.omteam.impl.entry.loginEntry
import com.omteam.impl.entry.mainEntry
import com.omteam.impl.entry.onboardingEntry
import com.omteam.impl.entry.otherEntry
import com.omteam.impl.entry.webViewEntry
import com.omteam.impl.screen.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: AutoLoginViewModel by viewModels()
    private val loginViewModel: com.omteam.impl.LoginViewModel by viewModels()
    
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
                    AutoLoginViewModel.AutoLoginState.Checking -> null
                    AutoLoginViewModel.AutoLoginState.NeedLogin -> LoginNavKey
                    AutoLoginViewModel.AutoLoginState.NeedOnboarding -> OnboardingNavKey(step = 1)
                    AutoLoginViewModel.AutoLoginState.NavigateToMain -> MainNavKey
                }

                // 자동 로그인 체크 중이면 스플래시 화면 표시
                if (initialNavKey == null) {
                    SplashScreen()
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

                            mainEntry(
                                onSignOut = {
                                    loginViewModel.logout()
                                    navigator.navigateToLogin()
                                },
                                onNavigateToOther = {
                                    navigator.navigateToOther()
                                },
                                onNavigateToEditMyGoal = {
                                    navigator.navigateToEditMyGoal()
                                },
                                onNavigateToEditMyInfo = {
                                    navigator.navigateToEditMyInfo()
                                }
                            )
                            
                            otherEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                },
                                onNavigateToWebView = { url ->
                                    navigator.navigateToWebView(url)
                                }
                            )
                            
                            webViewEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                }
                            )
                            
                            editMyGoalEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                }
                            )
                            
                            editMyInfoEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                },
                                onNavigateToEditExerciseTime = { exerciseTime ->
                                    navigator.navigateToEditExerciseTime(exerciseTime)
                                },
                                onNavigateToEditMissionTime = { missionTime ->
                                    navigator.navigateToEditMissionTime(missionTime)
                                },
                                onNavigateToEditFavoriteExercise = { favoriteExercises ->
                                    navigator.navigateToEditFavoriteExercise(favoriteExercises)
                                },
                                onNavigateToEditPattern = { pattern ->
                                    navigator.navigateToEditPattern(pattern)
                                }
                            )
                            
                            editExerciseTimeEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                }
                            )
                            
                            editMissionTimeEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                }
                            )
                            
                            editFavoriteExerciseEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                }
                            )
                            
                            editPatternEntry(
                                onBackClick = {
                                    navigator.popBackStack()
                                }
                            )
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