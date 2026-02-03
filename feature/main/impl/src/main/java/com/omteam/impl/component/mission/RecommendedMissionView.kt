package com.omteam.impl.component.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import timber.log.Timber
import java.time.LocalDate

/**
 * 오늘의 미션 상태 조회 API에서 미션을 받은 경우 표시
 */
@Composable
fun RecommendedMissionView(
    currentMission: CurrentMission,
    modifier: Modifier = Modifier,
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

            OMTeamText(
                text = currentMission.mission.name,
                style = PretendardType.button01Disabled,
                color = Gray08
            )
        }

        OMTeamButton(
            text = "미션 제안받기",
            onClick = {
                Timber.d("## 미션 제안받기 버튼 클릭")
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecommendedMissionViewPreview() {
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
            RecommendedMissionView(currentMission = mockMission)
        }
    }
}
