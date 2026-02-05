package com.omteam.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*

/**
 * 선택 가능한 카드 컴포넌트
 *
 * @param text 카드에 표시할 텍스트
 * @param isSelected 선택 여부
 * @param onClick 클릭 이벤트
 * @param modifier Modifier
 */
@Composable
fun OMTeamCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(dp64)
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp10))
            .background(
                color = if (isSelected) Green02 else GreenTab,
                shape = RoundedCornerShape(dp10)
            )
            .border(
                width = dp1,
                color = if (isSelected) Green04 else GreenGray05,
                shape = RoundedCornerShape(dp10)
            )
            .clickable(
                // 물결 효과 제거
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        OMTeamText(
            text = text,
            style = if (isSelected) PaperlogyType.cardTextEnabled else PaperlogyType.cardTextDisabled,
            color = if (isSelected) Gray12 else Gray09
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OMTeamCardSelectedPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.padding(dp20)
        ) {
            OMTeamCard(
                text = "선택된 카드",
                isSelected = true,
                onClick = {}
            )
            
            Spacer(modifier = Modifier.height(dp20))
            
            OMTeamCard(
                text = "선택되지 않은 카드",
                isSelected = false,
                onClick = {}
            )
        }
    }
}