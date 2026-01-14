package com.omteam.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*

/**
 * @param text 버튼 텍스트
 * @param onClick 버튼 클릭 시 호출되는 콜백
 * @param modifier Modifier (width 제어 가능)
 * @param height 버튼 높이
 * @param cornerRadius 모서리 둥글기
 * @param backgroundColor 배경색
 * @param textColor 텍스트 색상
 * @param enabled 버튼 활성화 여부
 */
@Composable
fun OMTeamButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = dp56,
    cornerRadius: Dp = dp10,
    backgroundColor: Color = Green04Button,
    textColor: Color = Gray09,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = PaperlogyType.button01,
            color = if (enabled) textColor else textColor.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, name = "OMTeamButton - Long 스타일")
@Composable
private fun OMTeamButtonLongPreview() {
    OMTeamTheme {
        OMTeamButton(
            text = "다음",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        )
    }
}

@Preview(showBackground = true, name = "OMTeamButton - Medium 스타일")
@Composable
private fun OMTeamButtonMediumPreview() {
    OMTeamTheme {
        OMTeamButton(
            text = "확인",
            onClick = {},
            height = dp60,
            cornerRadius = dp8,
            modifier = Modifier
                .width(dp200)
                .padding(dp20)
        )
    }
}

@Preview(showBackground = true, name = "OMTeamButton - Short 스타일")
@Composable
private fun OMTeamButtonShortPreview() {
    OMTeamTheme {
        OMTeamButton(
            text = "취소",
            onClick = {},
            height = dp60,
            cornerRadius = dp8,
            backgroundColor = GreenSub03Button,
            textColor = GreenSub07Button,
            modifier = Modifier
                .width(dp126)
                .padding(dp20)
        )
    }
}

@Preview(showBackground = true, name = "OMTeamButton - 모든 스타일")
@Composable
private fun OMTeamButtonAllStylesPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        ) {
            // 긴 버튼
            OMTeamButton(
                text = "가로로 길게 표시",
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dp16))

            // 다음, 이전 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dp10)
            ) {
                OMTeamButton(
                    text = "다음",
                    onClick = {},
                    height = dp60,
                    cornerRadius = dp8,
                    modifier = Modifier.width(dp200)
                )

                Spacer(modifier = Modifier.width(dp8))

                OMTeamButton(
                    text = "이전",
                    onClick = {},
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )
            }

            Spacer(modifier = Modifier.height(dp16))

            // 비활성화 상태
            OMTeamButton(
                text = "비활성화 버튼",
                onClick = { },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, name = "OMTeamButton - 긴 텍스트")
@Composable
private fun OMTeamButtonLongTextPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        ) {
            OMTeamButton(
                text = "아주 긴 텍스트는 한 줄로 표시됨123",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dp16))

            OMTeamButton(
                text = "매우 긴 텍스트 예시 123123",
                onClick = {},
                height = dp60,
                cornerRadius = dp8,
                modifier = Modifier.width(dp200)
            )

            Spacer(modifier = Modifier.height(dp16))

            OMTeamButton(
                text = "긴 텍스트",
                onClick = {},
                height = dp60,
                cornerRadius = dp8,
                backgroundColor = GreenSub03Button,
                textColor = GreenSub07Button,
                modifier = Modifier.width(dp126)
            )
        }
    }
}