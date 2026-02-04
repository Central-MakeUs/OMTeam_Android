package com.omteam.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R

@Composable
fun SubScreenHeader(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(dp24)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onBackClick()
                    },
                tint = Gray11
            )

            Spacer(modifier = Modifier.weight(1f))

            OMTeamText(
                text = title,
                style = PaperlogyType.headline02_2,
                color = Gray11
            )

            Spacer(modifier = Modifier.weight(1f))

            // 대칭 맞추기 위한 여백
            Box(modifier = Modifier.size(dp24))
        }

        // 하단 테두리 (양 옆 마진 없이 전체 너비)
        Spacer(
            modifier = Modifier
                .padding(top = dp12)
                .height(dp1)
                .background(Gray03)
                .layout { measurable, constraints ->
                    // 부모 패딩 무시하고 전체 너비 확장
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = constraints.maxWidth + dp40.roundToPx(),
                            maxWidth = constraints.maxWidth + dp40.roundToPx()
                        )
                    )

                    layout(placeable.width, placeable.height) {
                        placeable.place(-dp20.roundToPx(), 0)
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SubScreenHeaderPreview() {
    OMTeamTheme {
        SubScreenHeader(
            title = "테스트",
            onBackClick = {}
        )
    }
}