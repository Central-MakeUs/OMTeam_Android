package com.omteam.impl.tab.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.chip.ChipFlowRow
import com.omteam.designsystem.component.chip.InfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.onboarding.LifestyleType
import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.model.onboarding.WorkTimeType
import com.omteam.impl.component.SubScreenHeader
import com.omteam.impl.viewmodel.MyPageViewModel
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import com.omteam.omt.core.designsystem.R

@Composable
fun EditMyInfoScreen(
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToEditExerciseTime: (String) -> Unit = {},
    onNavigateToEditMissionTime: (String) -> Unit = {},
    onNavigateToEditFavoriteExercise: (List<String>) -> Unit = {},
    onNavigateToEditPattern: (String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val onboardingInfoState by myPageViewModel.onboardingInfoState.collectAsStateWithLifecycle()

    // 화면 진입 시 온보딩 정보 조회
    LaunchedEffect(Unit) {
        myPageViewModel.getOnboardingInfo()
    }

    EditMyInfoContent(
        modifier = modifier,
        onboardingInfoState = onboardingInfoState,
        onBackClick = onBackClick,
        onNavigateToEditExerciseTime = onNavigateToEditExerciseTime,
        onNavigateToEditMissionTime = onNavigateToEditMissionTime,
        onNavigateToEditFavoriteExercise = onNavigateToEditFavoriteExercise,
        onNavigateToEditPattern = onNavigateToEditPattern,
        onWithdraw = { myPageViewModel.withdraw() },
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun EditMyInfoContent(
    modifier: Modifier = Modifier,
    onboardingInfoState: MyPageOnboardingState = MyPageOnboardingState.Idle,
    onBackClick: () -> Unit = {},
    onNavigateToEditExerciseTime: (String) -> Unit = {},
    onNavigateToEditMissionTime: (String) -> Unit = {},
    onNavigateToEditFavoriteExercise: (List<String>) -> Unit = {},
    onNavigateToEditPattern: (String) -> Unit = {},
    onWithdraw: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    // 탈퇴 확인 다이얼로그 표시 상태
    var showWithdrawDialog by remember { mutableStateOf(false) }

    // 탈퇴 완료 다이얼로그 표시 상태
    var showWithdrawSuccessDialog by remember { mutableStateOf(false) }

    // 마지막으로 성공적으로 로드된 온보딩 데이터 캐시
    // 탈퇴 진행 중 Loading 상태로 전환되더라도 기존 표시되던 데이터 유지에 사용
    var cachedOnboardingData by remember { mutableStateOf<OnboardingInfo?>(null) }

    // 상태 변경 감지해서 데이터 캐시 갱신, 탈퇴 완료 다이얼로그 표시
    LaunchedEffect(onboardingInfoState) {
        when (onboardingInfoState) {
            is MyPageOnboardingState.Success -> cachedOnboardingData = onboardingInfoState.data
            is MyPageOnboardingState.UpdateSuccess -> cachedOnboardingData = onboardingInfoState.data
            is MyPageOnboardingState.WithdrawSuccess -> showWithdrawSuccessDialog = true
            else -> {}
        }
    }

    // 탈퇴 확인 다이얼로그
    if (showWithdrawDialog) {
        WithdrawDialog(
            onDismiss = { showWithdrawDialog = false },
            onConfirmWithdraw = {
                showWithdrawDialog = false
                onWithdraw()
            }
        )
    }

    // 탈퇴 완료 다이얼로그
    if (showWithdrawSuccessDialog) {
        WithdrawSuccessDialog(
            onDismiss = onNavigateToLogin
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(dp20)
    ) {
        SubScreenHeader(
            title = stringResource(R.string.edit_my_info_title),
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(dp36))

        // 현재 상태에서 데이터를 꺼내거나 캐시된 데이터를 fallback으로 사용
        // Loading(탈퇴 진행 중 포함) 상태에서도 기존에 표시되던 온보딩 정보 유지
        val displayData = when (onboardingInfoState) {
            is MyPageOnboardingState.Success -> onboardingInfoState.data
            is MyPageOnboardingState.UpdateSuccess -> onboardingInfoState.data
            else -> cachedOnboardingData
        }

        if (displayData != null) {
            // 운동 가능 시간
            val exerciseTimeString = getExerciseTimeString(displayData.availableStartTime)
            EditMyInfoItem(
                label = stringResource(R.string.available_exercise_hours),
                value = exerciseTimeString,
                onClick = { onNavigateToEditExerciseTime(exerciseTimeString) }
            )

            Spacer(modifier = Modifier.height(dp36))

            // 미션에 투자할 수 있는 시간
            EditMyInfoItem(
                label = stringResource(R.string.available_mission_hours),
                value = "${displayData.minExerciseMinutes}분",
                onClick = { onNavigateToEditMissionTime(displayData.minExerciseMinutes.toString()) }
            )

            Spacer(modifier = Modifier.height(dp36))

            // 선호 운동을 ","로 나눠서 개별 chip으로 표시
            val preferredExercises = displayData.preferredExerciseText
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            EditMyInfoItem(
                label = stringResource(R.string.favorite_exercises),
                chips = preferredExercises,
                onClick = { onNavigateToEditFavoriteExercise(preferredExercises) }
            )

            Spacer(modifier = Modifier.height(dp36))

            // 평소 생활 패턴
            val lifestyleTypeString = getLifestyleTypeString(displayData.lifestyleType)
            EditMyInfoItem(
                label = stringResource(R.string.usual_pattern),
                value = lifestyleTypeString,
                onClick = { onNavigateToEditPattern(lifestyleTypeString) }
            )
        } else {
            // 초기 로딩 중이거나 에러 시 기본값 표시
            EditMyInfoItem(
                label = stringResource(R.string.available_exercise_hours),
                value = "로딩 중...",
                onClick = { onNavigateToEditExerciseTime("") }
            )

            Spacer(modifier = Modifier.height(dp36))

            EditMyInfoItem(
                label = stringResource(R.string.available_mission_hours),
                value = "로딩 중...",
                onClick = { onNavigateToEditMissionTime("") }
            )

            Spacer(modifier = Modifier.height(dp36))

            EditMyInfoItem(
                label = stringResource(R.string.favorite_exercises),
                value = "로딩 중...",
                onClick = { onNavigateToEditFavoriteExercise(emptyList()) }
            )

            Spacer(modifier = Modifier.height(dp36))

            EditMyInfoItem(
                label = stringResource(R.string.usual_pattern),
                value = "로딩 중...",
                onClick = { onNavigateToEditPattern("") }
            )
        }

        Spacer(modifier = Modifier.height(dp62))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OMTeamText(
                text = stringResource(R.string.withdraw_message),
                style = PretendardType.body03_1,
                color = Gray07
            )
            Spacer(modifier = Modifier.weight(1f))
            OMTeamText(
                text = stringResource(R.string.withdraw),
                style = PretendardType.body03_1,
                color = Gray07,
                modifier = Modifier.clickable { showWithdrawDialog = true }
            )
        }
    }
}

/**
 * 탈퇴 확인 다이얼로그
 *
 * @param onDismiss 다이얼로그 닫기 콜백
 * @param onConfirmWithdraw 탈퇴 확인 콜백
 */
@Composable
private fun WithdrawDialog(
    onDismiss: () -> Unit,
    onConfirmWithdraw: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp20)
                .clip(RoundedCornerShape(dp12))
                .background(White)
                .padding(horizontal = dp14)
                .padding(top = dp42, bottom = dp24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OMTeamText(
                text = stringResource(R.string.withdraw_dialog_title),
                style = PaperlogyType.headline02_2,
                color = Gray12,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dp28))

            OMTeamText(
                text = stringResource(R.string.withdraw_dialog_message),
                style = PretendardType.body05,
                color = Gray09,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dp52))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dp8, Alignment.CenterHorizontally)
            ) {
                OMTeamButton(
                    text = stringResource(R.string.withdraw_confirm_button),
                    onClick = onConfirmWithdraw,
                    height = dp46,
                    cornerRadius = dp8,
                    backgroundColor = GreenGray05,
                    textColor = GreenGray09,
                    modifier = Modifier.weight(1f)
                )

                OMTeamButton(
                    text = stringResource(R.string.withdraw_cancel_button),
                    onClick = onDismiss,
                    height = dp46,
                    cornerRadius = dp8,
                    backgroundColor = Green07,
                    textColor = Gray12,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * 탈퇴 완료 다이얼로그
 *
 * @param onDismiss 다이얼로그 닫기 콜백 (확인 버튼 클릭 시 호출)
 */
@Composable
private fun WithdrawSuccessDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp20)
                .clip(RoundedCornerShape(dp12))
                .background(White)
                .padding(horizontal = dp14)
                .padding(top = dp42, bottom = dp24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OMTeamText(
                text = stringResource(R.string.withdraw_success_dialog_title),
                style = PaperlogyType.headline02_2,
                color = Gray12,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dp28))

            OMTeamText(
                text = stringResource(R.string.withdraw_success_dialog_message),
                style = PretendardType.body05,
                color = Gray09,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dp52))

            OMTeamButton(
                text = stringResource(R.string.withdraw_success_button),
                onClick = onDismiss,
                height = dp46,
                cornerRadius = dp8,
                backgroundColor = Green07,
                textColor = Gray12,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * availableStartTime 기준으로 운동 가능 시간 문자열 리소스 리턴
 */
@Composable
private fun getExerciseTimeString(startTime: String): String {
    val hour = startTime.take(2).toIntOrNull() ?: 18
    
    return when {
        hour < 18 -> stringResource(R.string.before_18)
        hour == 18 -> stringResource(R.string.after_18)
        hour == 19 -> stringResource(R.string.after_19)
        else -> stringResource(R.string.after_20)
    }
}

/**
 * LifestyleType -> 문자열 리소스 변환
 */
@Composable
private fun getLifestyleTypeString(lifestyleType: LifestyleType): String {
    return when (lifestyleType) {
        LifestyleType.REGULAR_DAYTIME -> stringResource(R.string.pattern_first)
        LifestyleType.IRREGULAR_OVERTIME -> stringResource(R.string.pattern_second)
        LifestyleType.SHIFT_NIGHT -> stringResource(R.string.pattern_third)
        LifestyleType.VARIABLE_DAILY -> stringResource(R.string.pattern_fourth)
    }
}

/**
 * 내 정보 수정 화면의 정보 아이템
 * 
 * @param label 라벨 텍스트 (예: "운동 가능 시간")
 * @param modifier Modifier
 * @param value 운동 가능 시간, 미션에 투자할 수 있는 시간, 평소 생활 패턴
 * @param chips 선호 운동. 3개만 설정 가능
 * @param onClick 클릭 이벤트
 */
@Composable
private fun EditMyInfoItem(
    label: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    chips: List<String> = emptyList(),
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        // 라벨, 화살표
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OMTeamText(
                text = label,
                style = PretendardType.body03_1,
                color = Gray07
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.icon_arrow_forth),
                contentDescription = "오른쪽 화살",
            )
        }

        Spacer(modifier = Modifier.height(dp10))

        if (value != null) {
            // Text 표시
            OMTeamText(
                text = value,
                style = PretendardType.button02Enabled,
                modifier = Modifier.padding(start = dp4)
            )
        } else if (chips.isNotEmpty()) {
            ChipFlowRow(
                chips = chips,
                modifier = Modifier.padding(start = dp4)
            )
        }

        Spacer(modifier = Modifier.height(dp8))

        // 구분선
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(dp1)
                .background(Gray05)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMyInfoScreenPreview() {
    OMTeamTheme {
        EditMyInfoContent(
            onboardingInfoState = MyPageOnboardingState.Success(
                OnboardingInfo(
                    nickname = "테스트",
                    appGoalText = "건강 관리",
                    workTimeType = WorkTimeType.FIXED,
                    availableStartTime = "19:00",
                    availableEndTime = "23:59",
                    minExerciseMinutes = 30,
                    preferredExerciseText = "헬스, 요가, 필라테스",
                    lifestyleType = LifestyleType.REGULAR_DAYTIME,
                    remindEnabled = true,
                    checkinEnabled = true,
                    reviewEnabled = true
                )
            )
        )
    }
}

@Preview(showBackground = true, name = "EditMyInfoItem - 텍스트")
@Composable
private fun EditMyInfoItemTextPreview() {
    OMTeamTheme {
        EditMyInfoItem(
            label = "운동 가능 시간",
            value = "19:00 이후부터"
        )
    }
}

@Preview(showBackground = true, name = "EditMyInfoItem - Chip")
@Composable
private fun EditMyInfoItemChipPreview() {
    OMTeamTheme {
        EditMyInfoItem(
            label = "선호 운동",
            chips = listOf("헬스", "요가", "필라테스")
        )
    }
}

@Preview(showBackground = true, name = "InfoChip")
@Composable
private fun InfoChipPreview() {
    OMTeamTheme {
        InfoChip(text = "헬스")
    }
}

@Preview(showBackground = true, name = "WithdrawDialog - 탈퇴 확인")
@Composable
private fun WithdrawDialogPreview() {
    OMTeamTheme {
        WithdrawDialog(
            onDismiss = {},
            onConfirmWithdraw = {}
        )
    }
}

@Preview(showBackground = true, name = "WithdrawSuccessDialog - 탈퇴 완료")
@Composable
private fun WithdrawSuccessDialogPreview() {
    OMTeamTheme {
        WithdrawSuccessDialog(
            onDismiss = {}
        )
    }
}