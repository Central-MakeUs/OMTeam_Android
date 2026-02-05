package com.omteam.impl.tab.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.component.chip.InfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.onboarding.LifestyleType
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
    onNavigateToEditPattern: (String) -> Unit = {}
) {
    val onboardingInfoState by myPageViewModel.onboardingInfoState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        myPageViewModel.getOnboardingInfo()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(dp20)
    ) {
        // 상단 헤더
        SubScreenHeader(
            title = stringResource(R.string.edit_my_info_title),
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(dp36))

        when (onboardingInfoState) {
            is MyPageOnboardingState.Success -> {
                val data = (onboardingInfoState as MyPageOnboardingState.Success).data
                
                // 운동 가능 시간
                val exerciseTimeString = getExerciseTimeString(data.availableStartTime)
                EditMyInfoItem(
                    label = stringResource(R.string.available_exercise_hours),
                    value = exerciseTimeString,
                    onClick = { onNavigateToEditExerciseTime(exerciseTimeString) }
                )

                Spacer(modifier = Modifier.height(dp36))

                // 미션에 투자할 수 있는 시간
                EditMyInfoItem(
                    label = stringResource(R.string.available_mission_hours),
                    value = "${data.minExerciseMinutes}분",
                    onClick = { onNavigateToEditMissionTime(data.minExerciseMinutes.toString()) }
                )

                Spacer(modifier = Modifier.height(dp36))

                // 선호 운동
                EditMyInfoItem(
                    label = stringResource(R.string.favorite_exercises),
                    chips = listOf(data.preferredExerciseText),
                    onClick = { onNavigateToEditFavoriteExercise(listOf(data.preferredExerciseText)) }
                )

                Spacer(modifier = Modifier.height(dp36))

                // 평소 생활 패턴
                val lifestyleTypeString = getLifestyleTypeString(data.lifestyleType)
                EditMyInfoItem(
                    label = stringResource(R.string.usual_pattern),
                    value = lifestyleTypeString,
                    onClick = { onNavigateToEditPattern(lifestyleTypeString) }
                )
            }
            else -> {
                // 로딩 중, 에러면 기본값 표시
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
                color = Gray07
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
@OptIn(ExperimentalLayoutApi::class)
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
            // Chip 표시
            // 최대 3개만 선택할 수 있어서 여기서 take(3)으로 제한 걸 필요 없음
            FlowRow(
                modifier = Modifier.padding(start = dp4),
                horizontalArrangement = Arrangement.spacedBy(dp8),
                verticalArrangement = Arrangement.spacedBy(dp8)
            ) {
                chips.forEach { chipText ->
                    InfoChip(text = chipText)
                }
            }
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
        EditMyInfoScreen()
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