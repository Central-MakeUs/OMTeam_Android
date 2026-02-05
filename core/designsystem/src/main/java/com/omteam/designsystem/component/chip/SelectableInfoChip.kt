package com.omteam.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*

/**
 * 선택 가능한 Chip 컴포넌트
 *
 * @param text Chip에 표시할 텍스트
 * @param isSelected 선택 여부
 * @param onClick 클릭 이벤트
 * @param modifier Modifier
 */
@Composable
fun SelectableInfoChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Gray01,
                shape = RoundedCornerShape(dp32)
            )
            .border(
                width = dp1,
                color = Gray03,
                shape = RoundedCornerShape(dp32)
            )
            .clickable(
                // 물결 효과 제거
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = dp12, vertical = dp8),
        contentAlignment = Alignment.Center
    ) {
        OMTeamText(
            text = text,
            style = if (isSelected) PretendardType.button02Enabled else PretendardType.button02Disabled,
            color = if (isSelected) Gray12 else Gray09
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectableInfoChipPreview() {
    OMTeamTheme {
        SelectableInfoChip(
            text = "헬스",
            isSelected = true,
            onClick = {}
        )
    }
}