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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.chip.InfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R

@Composable
fun EditMyInfoScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToEditExerciseTime: () -> Unit = {},
    onNavigateToEditMissionTime: () -> Unit = {},
    onNavigateToEditFavoriteExercise: () -> Unit = {},
    onNavigateToEditPattern: () -> Unit = {}
) {
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

        // 운동 가능 시간
        EditMyInfoItem(
            label = stringResource(R.string.available_exercise_hours),
            value = "19:00 이후부터",
            onClick = onNavigateToEditExerciseTime
        )

        Spacer(modifier = Modifier.height(dp36))

        // 미션에 투자할 수 있는 시간
        EditMyInfoItem(
            label = stringResource(R.string.available_mission_hours),
            value = "30분",
            onClick = onNavigateToEditMissionTime
        )

        Spacer(modifier = Modifier.height(dp36))

        // 선호 운동
        EditMyInfoItem(
            label = stringResource(R.string.favorite_exercises),
            chips = listOf("생활 속 운동", "스트레칭/요가"),
            onClick = onNavigateToEditFavoriteExercise
        )

        Spacer(modifier = Modifier.height(dp36))

        // 평소 생활 패턴
        EditMyInfoItem(
            label = stringResource(R.string.usual_pattern),
            value = "비교적 규칙적인 평일 주간 근무에요.",
            onClick = onNavigateToEditPattern
        )

        Spacer(modifier = Modifier.height(dp62))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OMTeamText(
                text = "회원정보를 삭제하시겠어요?",
                style = PretendardType.body03_1,
                color = Gray07
            )
            Spacer(modifier = Modifier.weight(1f))
            OMTeamText(
                text = "회원탈퇴",
                style = PretendardType.body03_1,
                color = Gray07
            )
        }
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
        modifier = modifier.fillMaxWidth()
    ) {
        // 라벨, 화살표
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() },
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