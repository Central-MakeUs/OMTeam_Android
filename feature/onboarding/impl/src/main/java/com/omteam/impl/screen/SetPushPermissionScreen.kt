package com.omteam.impl.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.impl.viewmodel.OnboardingViewModel
import com.omteam.impl.viewmodel.SubmitState
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.R

@Composable
fun SetPushPermissionScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onPushGranted: (String) -> Unit = {},
    onNavigateToMain: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val submitState by viewModel.submitState.collectAsStateWithLifecycle()

    // 온보딩 값 제출 상태 관찰
    LaunchedEffect(submitState) {
        when (submitState) {
            is SubmitState.Success -> {
                // 성공 시 메인 화면으로 이동
                onNavigateToMain()
                viewModel.resetSubmitState()
            }
            is SubmitState.Error -> {
                // 에러 메시지 표시
                viewModel.resetSubmitState()
            }
            else -> {}
        }
    }

    SetPushPermissionScreenContent(
        submitState = submitState,
        onPushGranted = onPushGranted,
        onBack = onBack,
        onSubmitOnboarding = { viewModel.submitOnboarding() },
        onUpdatePushPermission = { isGranted -> viewModel.updatePushPermission(isGranted) }
    )
}

@Composable
fun SetPushPermissionScreenContent(
    submitState: SubmitState,
    onPushGranted: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onSubmitOnboarding: () -> Unit = {},
    onUpdatePushPermission: (Boolean) -> Unit = {}
) {
    var selectedFavoriteExercise by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val grantText = stringResource(R.string.grant)
    val notGrantText = stringResource(R.string.not_grant)

    // 온보딩 값 제출 상태 관찰
    LaunchedEffect(submitState) {
        when (submitState) {
            is SubmitState.Error -> {
                // 에러 메시지 표시
                errorMessage = (submitState as SubmitState.Error).message
                showError = true
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OMTeamText(
                    text = stringResource(R.string.set_push_permission_screen_title),
                    style = PaperlogyType.headline02
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamText(
                    text = stringResource(R.string.set_push_permission_screen_subtitle),
                    style = PaperlogyType.headline02
                )

                Spacer(modifier = Modifier.height(dp20))

                OMTeamCard(
                    text = grantText,
                    isSelected = (selectedFavoriteExercise == grantText),
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        val isGranted = if (selectedFavoriteExercise == grantText) {
                            selectedFavoriteExercise = ""
                            false
                        } else {
                            selectedFavoriteExercise = grantText
                            true
                        }
                        onUpdatePushPermission(isGranted)
                        onPushGranted(selectedFavoriteExercise)
                    },
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = notGrantText,
                    isSelected = (selectedFavoriteExercise == notGrantText),
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        val isGranted = if (selectedFavoriteExercise == notGrantText) {
                            selectedFavoriteExercise = ""
                            false
                        } else {
                            selectedFavoriteExercise = notGrantText
                            false  // "허용 안 함" 선택 시 false
                        }
                        onUpdatePushPermission(isGranted)
                        onPushGranted(selectedFavoriteExercise)
                    }
                )

                // 버튼과 안 겹치게 여백 추가
                Spacer(modifier = Modifier.height(dp20))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dp20),
                horizontalArrangement = Arrangement.Center,
            ) {
                OMTeamButton(
                    text = stringResource(R.string.before),
                    onClick = { onBack() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                Spacer(modifier = Modifier.width(dp9))

                OMTeamButton(
                    text = stringResource(R.string.next),
                    onClick = {
                        if (selectedFavoriteExercise.isNotEmpty()) {
                            // 온보딩 정보 제출
                            onSubmitOnboarding()
                        }
                    },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = if (selectedFavoriteExercise.isNotEmpty()) Green07 else Green04,
                    modifier = Modifier.width(dp200),
                    enabled = selectedFavoriteExercise.isNotEmpty() && submitState !is SubmitState.Loading
                )
            }
        }

        // 로딩 표시
        if (submitState is SubmitState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Green07)
            }
        }

        // 에러 메시지 표시
        if (showError) {
            // TODO: 에러 다이얼로그 또는 스낵바 표시
            // 현재는 로그로만 표시
            LaunchedEffect(Unit) {
                showError = false
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SetPushPermissionScreenContentPreview() {
    SetPushPermissionScreenContent(
        submitState = SubmitState.Idle,
        onPushGranted = {},
        onBack = {},
        onSubmitOnboarding = {},
        onUpdatePushPermission = {}
    )
}