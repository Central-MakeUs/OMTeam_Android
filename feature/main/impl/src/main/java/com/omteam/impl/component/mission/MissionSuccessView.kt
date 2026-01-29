package com.omteam.impl.component.mission

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.mission.DailyMissionStatus

@Composable
fun MissionSuccessView(
    missionStatus: DailyMissionStatus
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = dp32)
    ) {
        Box(
            modifier = Modifier
                .size(dp81)
                .background(
                    color = Green05,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(dp16))

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 21.5.dp)
        ) {
            missionStatus.currentMission?.let { currentMission ->
                OMTeamText(
                    text = currentMission.mission.name,
                    style = PaperlogyType.body01
                )
            } ?: run {
                OMTeamText(
                    text = "오늘의 미션이 완료되었어요!",
                    style = PaperlogyType.body01
                )
            }
        }
    }
}