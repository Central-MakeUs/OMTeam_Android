package com.omteam.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.Gray09
import com.omteam.designsystem.theme.Green01
import com.omteam.designsystem.theme.Green04
import com.omteam.designsystem.theme.Green04Button
import com.omteam.designsystem.theme.GreenCard01
import com.omteam.designsystem.theme.GreenSub03
import com.omteam.designsystem.theme.GreenSub03Card
import com.omteam.designsystem.theme.PaperlogyFont
import com.omteam.designsystem.theme.PaperlogyType
import com.omteam.designsystem.theme.White

@Composable
fun OMTeamCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = PaperlogyType.headline03,
) {
    Box(
        modifier = modifier
            .height(dp64)
            .fillMaxWidth()  // align-self: stretch
            .clip(RoundedCornerShape(dp10))
            .background(
                color = if (isSelected) Green04 else Green01, // 선택 시 색 변경 옵션
                shape = RoundedCornerShape(dp10)
            )
            .border(
                width = dp1,
                color = GreenSub03Card,  // #B5F5E5
                shape = RoundedCornerShape(dp10)
            )
            .clickable { onClick() }
            .padding(
                horizontal = dp14,
            ),
        contentAlignment = Alignment.Center
    ) {
        OMTeamText(
            text = text,
            style = textStyle,
            color = Gray09
        )
    }
}