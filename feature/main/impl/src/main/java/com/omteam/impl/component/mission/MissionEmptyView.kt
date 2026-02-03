package com.omteam.impl.component.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R

@Composable
fun MissionEmptyView(
    title: String = stringResource(R.string.mission_not_created_title),
    description: String = stringResource(R.string.mission_not_created_description)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = dp32)
    ) {
        Image(
            painter = painterResource(id = R.drawable.character_embarrassed_green_apple),
            contentDescription = "캐릭터 이미지",
            modifier = Modifier
                .size(dp84),
        )

        Spacer(modifier = Modifier.width(dp16))

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 21.5.dp)
        ) {
            OMTeamText(
                text = title,
                style = PaperlogyType.body01
            )

            Spacer(modifier = Modifier.height(dp6))

            OMTeamText(
                text = description,
                style = PretendardType.body04_3,
                color = Gray09
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MissionEmptyViewPreview() {
    OMTeamTheme {
        Column {
            MissionEmptyView(
                title = "아직 미션이 생성되지 않았어요!",
                description = "개인 설정을 완료하여 미션을 받아보세요."
            )
        }
    }
}