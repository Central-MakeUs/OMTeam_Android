package com.omteam.designsystem.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.omteam.designsystem.foundation.dp8
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.PaperlogyType
import com.omteam.designsystem.theme.PretendardType

@Composable
fun OMTeamText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = PaperlogyType.headline02,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    textDecoration: TextDecoration? = null,
    onClick: (() -> Unit)? = null,
) {
    Text(
        modifier = modifier.then(
            if (onClick != null) {
                Modifier.clickable { onClick() }
            } else {
                Modifier
            }
        ),
        text = text,
        style = style.copy(
            // style 기본값 유지하면서 명시적으로 넘어온 값만 재정의
            color = if (color != Color.Unspecified) color else style.color,
            fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
            fontWeight = fontWeight ?: style.fontWeight,
            letterSpacing = if (letterSpacing != TextUnit.Unspecified) letterSpacing else style.letterSpacing,
            lineHeight = if (lineHeight != TextUnit.Unspecified) lineHeight else style.lineHeight,
            textDecoration = textDecoration ?: style.textDecoration,
        ),
        textAlign = textAlign,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
    )
}

@Preview
@Composable
private fun OMTeamTextPreview() {
    OMTeamTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(dp8),
            modifier = Modifier.background(Color.White)
        ) {
            OMTeamText(
                text = "OMT에서 사용하실\n닉네임을 입력해주세요!"
            )
            OMTeamText(
                text = "다음",
                style = PaperlogyType.button01
            )
            OMTeamText(
                text = "닉네임을 입력해주세요. (최대 OO자)",
                style = PretendardType.button03Disabled
            )
        }
    }
}