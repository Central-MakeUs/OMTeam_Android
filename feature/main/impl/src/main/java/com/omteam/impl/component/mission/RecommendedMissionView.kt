package com.omteam.impl.component.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import com.omteam.domain.model.mission.CurrentMission
import com.omteam.domain.model.mission.Mission
import com.omteam.domain.model.mission.MissionStatus
import com.omteam.domain.model.mission.MissionType
import com.omteam.omt.core.designsystem.R
import java.time.LocalDate

/**
 * 오늘의 미션 상태를 표시하는 뷰
 *
 * @param currentMission 현재 미션 정보 (null이면 미션 제안 대기 상태)
 * @param isMissionCompleted 미션 성공 완료 여부
 * @param onRequestMissionClick 미션 제안받기 클릭 콜백
 * @param onVerifyMissionClick 미션 인증하기 클릭 콜백
 */
@Composable
fun RecommendedMissionView(
    currentMission: CurrentMission?,
    isMissionCompleted: Boolean = false,
    modifier: Modifier = Modifier,
    onRequestMissionClick: () -> Unit = {},
    onVerifyMissionClick: (actionType: String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp10))
            .background(GreenTab)
            .padding(start = dp12, end = dp12, top = dp12, bottom = dp14),
        verticalArrangement = Arrangement.spacedBy(dp16)
    ) {
        // 상단 뷰 (미션 정보 영역)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dp10))
                .background(White)
                .padding(start = dp8, end = dp8, top = dp8, bottom = dp14)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(dp4))
                    .background(Yellow02)
                    .padding(dp4),
                contentAlignment = Alignment.Center
            ) {
                OMTeamText(
                    text = "미션 01",
                    style = PretendardType.body04_2,
                    color = Yellow07,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(dp10))

            // 미션 성공 완료 시 "내일 다시 만나요!", 체크 이미지 표시
            if (isMissionCompleted) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OMTeamText(
                        text = "내일 다시 만나요!",
                        style = PretendardType.button01Enabled,
                        color = Gray08
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.check_able),
                        contentDescription = "미션 완료",
                        modifier = Modifier.size(dp24)
                    )
                }
            } else {
                // 현재 진행 중인 미션이 null이면 채팅을 통해 미션을 받아보세요, 아니면 미션 이름 표시
                OMTeamText(
                    text = currentMission?.mission?.name ?: "채팅을 통해 미션을 받아보세요!",
                    style = if (currentMission != null) PretendardType.button01Enabled else PretendardType.button01Disabled,
                    color = if (currentMission != null) Gray12 else Gray08
                )
            }
        }

        // currentMission이 null이면 "미션 제안받기", 있으면 "미션 인증하기" 버튼 표시
        // 미션 성공 완료 시 버튼 비활성화
        OMTeamButton(
            text = if (isMissionCompleted) {
                "미션 완료"
            } else if (currentMission == null) {
                "미션 제안받기"
            } else {
                "미션 인증하기"
            },
            onClick = {
                if (currentMission == null) {
                    onRequestMissionClick()
                } else {
                    // 미션 인증 시 COMPLETE_MISSION 액션 타입만 전달
                    onVerifyMissionClick("COMPLETE_MISSION")
                }
            },
            enabled = !isMissionCompleted,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// 미션이 있는 경우 Preview
@Preview(showBackground = true)
@Composable
private fun RecommendedMissionViewWithMissionPreview() {
    val mockMission = CurrentMission(
        recommendedMissionId = 1,
        missionDate = LocalDate.now(),
        status = MissionStatus.RECOMMENDED,
        mission = Mission(
            id = 1,
            name = "30분 걷기",
            type = MissionType.EXERCISE,
            difficulty = 2,
            estimatedMinutes = 30,
            estimatedCalories = 150
        )
    )

    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(dp20)
        ) {
            RecommendedMissionView(
                currentMission = mockMission,
                isMissionCompleted = false,
                onVerifyMissionClick = { actionType -> }
            )
        }
    }
}

// 미션 성공 완료 경우 Preview
@Preview(showBackground = true, name = "Mission Completed")
@Composable
private fun RecommendedMissionViewCompletedPreview() {
    val mockMission = CurrentMission(
        recommendedMissionId = 1,
        missionDate = LocalDate.now(),
        status = MissionStatus.COMPLETED,
        mission = Mission(
            id = 1,
            name = "30분 걷기",
            type = MissionType.EXERCISE,
            difficulty = 2,
            estimatedMinutes = 30,
            estimatedCalories = 150
        )
    )

    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(dp20)
        ) {
            RecommendedMissionView(
                currentMission = mockMission,
                isMissionCompleted = true,
                onVerifyMissionClick = { actionType -> }
            )
        }
    }
}

// 미션이 없는 경우 Preview
@Preview(showBackground = true)
@Composable
private fun RecommendedMissionViewWithoutMissionPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(dp20)
        ) {
            RecommendedMissionView(currentMission = null)
        }
    }
}
