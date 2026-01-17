package com.omteam.impl.tab

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 스크롤 가능한 컨텐츠 영역
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(dp20)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.screen_inner_logo),
                contentDescription = "왼쪽 상단 로고",
                modifier = Modifier
                    .size(dp50)
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(dp134))

            SimpleChatBubbleWithX(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(dp48))

            OMTeamText(
                text = stringResource(com.omteam.main.impl.R.string.chat_screen_middle_text),
                style = PretendardType.button02Disabled,
                color = Gray10,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(dp20))
        }

        // 고정된 버튼 영역
        OMTeamButton(
            text = stringResource(com.omteam.main.impl.R.string.chat_screen_button),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        )
    }
}

@Composable
fun SimpleChatBubbleWithX(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(180.dp, 170.dp)) {
        Canvas(modifier = Modifier
            .size(160.dp)
            .offset(x = 0.dp, y = 10.dp)
        ) {
            drawCircle(color = Color(0xFFD9D9D9))
        }

        // 녹색 말풍선 전체
        // 위치 옮기려면 offset 수정
        Canvas(
            modifier = Modifier
                .size(48.dp, 54.dp)
                .offset(x = 130.dp, y = 0.dp)
        ) {
            val path = Path().apply {
                // 원 그리기
                addOval(
                    Rect(
                        left = 0f,
                        top = 0f,
                        right = 48.dp.toPx(),
                        bottom = 48.dp.toPx()
                    )
                )

                // 왼쪽 밑 꼬리
                moveTo(2.4.dp.toPx(), 34.5.dp.toPx())    // 원의 왼쪽 하단
                lineTo(0.8.dp.toPx(), 45.8.dp.toPx())    // 꼬리 끝
                lineTo(2.3.dp.toPx(), 48.dp.toPx())      // 베이스 시작
                lineTo(24.dp.toPx(), 48.dp.toPx())       // 베이스 끝
                lineTo(24.dp.toPx(), 34.5.dp.toPx())     // 원으로 복귀
                close()
            }

            drawPath(path, color = Color(0xFF45E7BD))
        }

        // X 아이콘
        // 위치 옮기려면 offset 수정
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color(0xFF0B2F1F),
            modifier = Modifier
                .offset(x = 144.dp, y = 14.dp)
                .size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreen()
}