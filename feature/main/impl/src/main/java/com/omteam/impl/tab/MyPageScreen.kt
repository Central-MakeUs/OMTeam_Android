package com.omteam.impl.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R
import timber.log.Timber

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(dp20)
            .verticalScroll(rememberScrollState()),
    ) {
        Image(
            painter = painterResource(id = R.drawable.screen_inner_logo),
            contentDescription = "왼쪽 상단 로고",
            modifier = Modifier.size(dp50)
        )

        Spacer(modifier = Modifier.height(dp32))

        // 프로필 이미지 + 편집 아이콘
        Box(
            modifier = Modifier
                .size(dp140)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .size(dp140)
                    .background(
                        color = ErrorBottomSheetBackground,
                        shape = CircleShape
                    )
            )

            // 편집 아이콘
            Image(
                painter = painterResource(id = R.drawable.mypage_edit_able),
                contentDescription = "프로필 편집",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = dp8)
                    .size(dp36)
            )
        }

        Spacer(modifier = Modifier.height(dp16))

        // TODO : 닉네임을 카톡 / 구글 로그인 성공 후 클라가 여까지 땡겨오는 건지 or 서버에서 따로 주는지 확인
        OMTeamText(
            text = "닉네임",
            style = PaperlogyType.headline03,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(dp53))

        // 나의 목표, 수정하기
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OMTeamText(
                text = "나의 목표",
                style = PaperlogyType.headline04
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .height(dp32)
                    .background(
                        color = Green03,
                        shape = RoundedCornerShape(dp4)
                    )
                    .padding(horizontal = dp10)
                    .clickable {
                        Timber.d("## 수정하기 클릭")
                    },
                contentAlignment = Alignment.Center
            ) {
                OMTeamText(
                    text = stringResource(com.omteam.main.impl.R.string.edit_button),
                    style = PretendardType.button03Disabled.copy(
                        fontSize = 14.sp,
                        letterSpacing = (-0.056).sp
                    ),
                    color = Gray09,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(dp10))

        // 운동 습관 형성 카드
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dp91)
                .background(
                    color = Gray02,
                    shape = RoundedCornerShape(dp10)
                )
                .padding(horizontal = dp12, vertical = dp27)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_exercise),
                    contentDescription = "운동 습관 형성 아이콘",
                    modifier = Modifier.size(dp37)
                )

                Spacer(modifier = Modifier.width(dp12))

                OMTeamText(
                    text = stringResource(com.omteam.main.impl.R.string.my_page_card),
                    style = PaperlogyType.headline03,
                    color = Gray11
                )
            }
        }

        Spacer(modifier = Modifier.height(dp50))

        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.setting_alarm_title),
            onClick = { Timber.d("## 알림 설정하기 클릭") }
        )
        MyPageMenuDivider()
        
        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.edit_my_info),
            onClick = { Timber.d("## 내 정보 수정하기 클릭") }
        )
        MyPageMenuDivider()
        
        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.inquiry),
            onClick = { Timber.d("## 문의하기 클릭") }
        )
        MyPageMenuDivider()
        
        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.other),
            onClick = { Timber.d("## 기타 클릭") },
        )
        MyPageMenuDivider()

        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.logout),
            onClick = onSignOut,
            showDivider = false
        )
    }
}

@Composable
private fun MyPageMenuItem(
    text: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        OMTeamText(
            text = text,
            style = PretendardType.body02_2,
            color = Gray11,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    // 물결 효과 제거
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
        )

        if (showDivider) {
            Spacer(modifier = Modifier.height(dp18))
        }
    }
}

@Composable
private fun MyPageMenuDivider() {
    Spacer(
        modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth()
            .background(Gray03)
    )
    Spacer(modifier = Modifier.height(dp18))
}

@Preview(showBackground = true)
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen()
}