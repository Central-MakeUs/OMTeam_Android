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
import com.omteam.designsystem.foundation.dp16
import com.omteam.designsystem.foundation.dp32
import com.omteam.designsystem.foundation.dp6
import com.omteam.designsystem.foundation.dp81
import com.omteam.designsystem.theme.ErrorBottomSheetBackground
import com.omteam.designsystem.theme.Gray09
import com.omteam.designsystem.theme.PaperlogyType
import com.omteam.designsystem.theme.PretendardType

@Composable
fun MissionEmptyView(
    title: String,
    description: String
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
                    color = ErrorBottomSheetBackground,
                    shape = CircleShape
                )
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