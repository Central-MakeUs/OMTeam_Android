package com.omteam.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.Gray05
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.PretendardType
import com.omteam.designsystem.theme.Yellow14
import com.omteam.omt.core.designsystem.R

@Composable
fun OMTeamSnsButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    text: String,
    iconSize: Dp = dp24,
    onClick: () -> Unit,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
) {
    val icon = painterResource(id = iconRes)
    val border = if (text == "Google로 계속하기") {
        BorderStroke(
            width = 1.dp,
            color = Gray05
        )
    } else {
        null
    }

    Button(
        modifier = modifier.fillMaxWidth()
            .height(dp56),
        shape = RoundedCornerShape(dp12),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = border,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = icon,
                contentDescription = "$text 버튼",
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.CenterStart),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(dp12))
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                style = PretendardType.button01Disabled
            )
        }
    }
}

@Preview
@Composable
private fun OMTeamSnsButtonPreview() {
    OMTeamTheme {
        Column {
            OMTeamSnsButton(
                iconRes = R.drawable.kakao_symbol,
                text = "카카오로 계속하기",
                onClick = {},
                backgroundColor = Yellow14,
                contentColor = Color.Black
            )
            Spacer(modifier = Modifier.height(dp12))
            OMTeamSnsButton(
                iconRes = R.drawable.google_symbol,
                text = "Google로 계속하기",
                onClick = {},
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        }
    }
}