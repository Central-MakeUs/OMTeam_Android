package com.omteam.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.dp1
import com.omteam.designsystem.foundation.dp12
import com.omteam.designsystem.foundation.dp32
import com.omteam.designsystem.foundation.dp8
import com.omteam.designsystem.theme.Gray01
import com.omteam.designsystem.theme.Gray03
import com.omteam.designsystem.theme.Gray12
import com.omteam.designsystem.theme.PretendardType

/**
 * 내 정보 수정하기 > 선호 운동을 표시하기 위한 Chip
 */
@Composable
fun InfoChip(
    text: String,
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
            .padding(horizontal = dp12, vertical = dp8),
        contentAlignment = Alignment.Center
    ) {
        OMTeamText(
            text = text,
            style = PretendardType.button02Enabled,
            color = Gray12
        )
    }
}