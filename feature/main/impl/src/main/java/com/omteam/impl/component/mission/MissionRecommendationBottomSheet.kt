package com.omteam.impl.component.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.mission.Mission
import com.omteam.domain.model.mission.MissionStatus
import com.omteam.domain.model.mission.MissionType
import com.omteam.domain.model.mission.RecommendedMission
import com.omteam.omt.core.designsystem.R
import java.time.LocalDate

/**
 * 미션 추천 바텀 시트
 *
 * @param recommendations 추천 미션 목록
 * @param onDismiss 바텀 시트 닫기 콜백
 * @param onMissionSelect 미션 선택 콜백
 * @param onRetryClick 다시 제안받기 버튼 클릭 콜백
 * @param onStartMissionClick 미션 시작하기 버튼 클릭 콜백
 */
@Composable
fun MissionRecommendationBottomSheetContent(
    recommendations: List<RecommendedMission>,
    onDismiss: () -> Unit,
    onMissionSelect: (RecommendedMission) -> Unit,
    onRetryClick: () -> Unit = {},
    onStartMissionClick: () -> Unit = {}
) {
    var selectedMissionId by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp20)
        ) {
            // 닫기 아이콘
            Image(
                painter = painterResource(id = R.drawable.icon_close),
                contentDescription = "닫기",
                modifier = Modifier
                    .align(Alignment.End)
                    .size(dp24)
                    .clickable { onDismiss() }
            )

            Spacer(modifier = Modifier.height(dp15))

            // 제목 영역
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_mission),
                    contentDescription = "미션 아이콘",
                    modifier = Modifier.size(dp32)
                )

                Spacer(modifier = Modifier.width(dp8))

                OMTeamText(
                    text = "오늘 도전해볼 미션을 선택하세요!",
                    style = PaperlogyType.headline03,
                    color = Gray12
                )
            }

            Spacer(modifier = Modifier.height(dp12))

            // 스크롤 가능한 미션 카드 목록
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dp12)
            ) {
                recommendations.forEach { recommendation ->
                    MissionRecommendationCard(
                        recommendation = recommendation,
                        isSelected = selectedMissionId == recommendation.recommendedMissionId,
                        onClick = {
                            selectedMissionId = recommendation.recommendedMissionId
                            onMissionSelect(recommendation)
                        }
                    )
                }
            }
        }

        // 버튼 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(GreenSub01)
                .padding(top = dp1)
        ) {
            // 상단 테두리
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dp1)
                    .background(GreenSub03)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenSub01)
                    .padding(horizontal = dp20, vertical = dp16),
                horizontalArrangement = Arrangement.spacedBy(dp10, Alignment.CenterHorizontally)
            ) {
                OMTeamButton(
                    text = "다시 제안받기",
                    onClick = onRetryClick,
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                OMTeamButton(
                    text = "미션 시작하기",
                    onClick = onStartMissionClick,
                    height = dp60,
                    cornerRadius = dp8,
                    modifier = Modifier.width(dp200)
                )
            }

            Spacer(modifier = Modifier.height(dp44))
        }
    }
}

/**
 * 미션 추천 카드
 */
@Composable
private fun MissionRecommendationCard(
    recommendation: RecommendedMission,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Green03 else Gray03
    val backgroundColor = if (isSelected) Green01 else Gray01

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .border(
                width = dp1,
                color = borderColor,
                shape = RoundedCornerShape(dp12)
            )
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(dp12)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 미션 타입 뱃지 + 난이도
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 미션 타입 뱃지
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(dp4))
                        .background(Yellow02)
                        .padding(dp4),
                    contentAlignment = Alignment.Center
                ) {
                    OMTeamText(
                        text = getMissionTypeName(recommendation.mission.type),
                        style = PretendardType.body04_2,
                        color = Yellow07,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 난이도 텍스트
                OMTeamText(
                    text = "난이도",
                    style = PretendardType.body04_1,
                    color = Gray09
                )

                Spacer(modifier = Modifier.width(dp3))

                // 별 아이콘들
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dp4)
                ) {
                    repeat(5) { index ->
                        val isAbled = index < recommendation.mission.difficulty
                        Image(
                            painter = painterResource(
                                id = if (isAbled) R.drawable.icon_star_abled else R.drawable.icon_star_disabled
                            ),
                            contentDescription = if (isAbled) "활성화된 별" else "비활성화된 별",
                            modifier = Modifier.size(dp12)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dp14))

            // 미션 이름
            OMTeamText(
                text = recommendation.mission.name,
                style = PaperlogyType.headline04,
                color = Gray08
            )

            Spacer(modifier = Modifier.height(dp6))

            // 소요 시간 + 칼로리
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_clock),
                    contentDescription = "시간 아이콘",
                    modifier = Modifier.size(dp12)
                )

                Spacer(modifier = Modifier.width(dp3))

                OMTeamText(
                    text = "${recommendation.mission.estimatedMinutes} min",
                    style = PretendardType.body04_2,
                    color = Gray05
                )

                Spacer(modifier = Modifier.width(dp8))

                Image(
                    painter = painterResource(id = R.drawable.icon_calorie),
                    contentDescription = "칼로리 아이콘",
                    modifier = Modifier.size(dp12)
                )

                Spacer(modifier = Modifier.width(dp3))

                OMTeamText(
                    text = "${recommendation.mission.estimatedCalories} kcal",
                    style = PretendardType.body04_2,
                    color = Gray05
                )
            }
        }
    }
}

/**
 * 미션 타입을 한글 이름으로 변환
 */
private fun getMissionTypeName(type: MissionType): String {
    return when (type) {
        MissionType.EXERCISE -> "운동 미션"
        MissionType.DIET -> "식단 미션"
        MissionType.UNKNOWN -> "미션"
    }
}

@Preview(showBackground = true)
@Composable
private fun MissionRecommendationBottomSheetPreview() {
    val mockRecommendations = listOf(
        RecommendedMission(
            recommendedMissionId = 1,
            missionDate = LocalDate.now(),
            status = MissionStatus.RECOMMENDED,
            mission = Mission(
                id = 1,
                name = "저녁 6시 30분, 10분 홈 트레이닝 시작",
                type = MissionType.EXERCISE,
                difficulty = 1,
                estimatedMinutes = 10,
                estimatedCalories = 40
            )
        ),
        RecommendedMission(
            recommendedMissionId = 2,
            missionDate = LocalDate.now(),
            status = MissionStatus.RECOMMENDED,
            mission = Mission(
                id = 2,
                name = "야근 중 5분 스탠딩 스트레칭",
                type = MissionType.EXERCISE,
                difficulty = 2,
                estimatedMinutes = 5,
                estimatedCalories = 20
            )
        ),
        RecommendedMission(
            recommendedMissionId = 3,
            missionDate = LocalDate.now(),
            status = MissionStatus.RECOMMENDED,
            mission = Mission(
                id = 3,
                name = "저녁 식사 전 물 1잔 마시기",
                type = MissionType.DIET,
                difficulty = 1,
                estimatedMinutes = 2,
                estimatedCalories = 0
            )
        )
    )

    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
        ) {
            MissionRecommendationBottomSheetContent(
                recommendations = mockRecommendations,
                onDismiss = {},
                onMissionSelect = {},
                onRetryClick = {},
                onStartMissionClick = {}
            )
        }
    }
}
