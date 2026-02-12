package com.omteam.impl.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.datastore.PermissionDataStore
import com.omteam.designsystem.foundation.*
import com.omteam.impl.screen.component.OnboardingStepIndicator
import com.omteam.impl.viewmodel.NicknameErrorType
import com.omteam.impl.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    currentStep: Int,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToMain: () -> Unit = {},
    permissionDataStore: PermissionDataStore,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val onboardingData by viewModel.onboardingData.collectAsState()
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(dp72))
        // 1~7단계 숫자
        OnboardingStepIndicator(currentStep = currentStep)
        Spacer(modifier = Modifier.height(dp36))

        // 나머지 온보딩 컨텐츠
        // 좌우 20dp씩 떨어져서 표시돼야 함
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = dp20)
        ) {
            when (currentStep) {
                1 -> SetNicknameScreen(
                    viewModel = viewModel,
                    onNext = {
                        if (onboardingData.nickname.isNotEmpty() && viewModel.nicknameErrorType.value == NicknameErrorType.NONE) {
                            onNext()
                        }
                    },
                )
                2 -> SetGoalScreen(
                    initialGoal = onboardingData.goal,
                    onGoalChange = { viewModel.updateGoal(it) },
                    onNext = {
                        if (onboardingData.goal.isNotEmpty()) {
                            onNext()
                        }
                    },
                    onBack = onBack,
                )
                3 -> SetTimeScreen(
                    onTimeChange = { viewModel.updateTime(it) },
                    onNext = {
                        if (onboardingData.time.isNotEmpty()) {
                            onNext()
                        }
                    },
                    onBack = onBack,
                )
                4 -> SetMissionTimeScreen(
                    onMissionTimeChange = { viewModel.updateMissionTime(it) },
                    onNext = {
                        if (onboardingData.missionTime.isNotEmpty()) {
                            onNext()
                        }
                    },
                    onBack = onBack,
                )
                5 -> SetFavoriteExerciseScreen(
                    onExerciseChange = { viewModel.updateFavoriteExercise(it) },
                    onNext = {
                        if (onboardingData.favoriteExercise.isNotEmpty()) {
                            onNext()
                        }
                    },
                    onBack = onBack,
                )
                6 -> SetSimilarPatternScreen(
                    onPatternChange = { viewModel.updatePattern(it) },
                    onNext = {
                        if (onboardingData.pattern.isNotEmpty()) {
                            onNext()
                        }
                    },
                    onBack = onBack,
                )
                7 -> SetPushPermissionScreen(
                    viewModel = viewModel,
                    permissionDataStore = permissionDataStore,
                    onNavigateToMain = onNavigateToMain,
                    onBack = onBack,
                )
            }
        }
    }
}