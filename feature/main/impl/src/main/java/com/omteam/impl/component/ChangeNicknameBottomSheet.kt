package com.omteam.impl.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.component.textfield.OMTeamTextField
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R

/**
 * 닉네임 변경 바텀 시트 콘텐츠
 *
 * @param onDismiss 바텀 시트 닫기 콜백
 * @param onNicknameChange 닉네임 변경 콜백
 * @param onFocusChanged 텍스트 필드 포커스 변경 콜백
 */
@Composable
fun ChangeNicknameBottomSheetContent(
    onDismiss: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit
) {
    var nickname by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dp20)
            .padding(bottom = dp20)
    ) {
        // 닫기 아이콘
        Image(
            painter = painterResource(id = R.drawable.icon_close),
            contentDescription = "닫기",
            modifier = Modifier
                .align(Alignment.End)
                .size(dp24)
                .clickable { onDismiss() }
        )

        Spacer(modifier = Modifier.height(dp16))

        // 닉네임 변경하기 제목
        OMTeamText(
            text = "닉네임 변경하기",
            style = PaperlogyType.headline03,
            color = Gray12
        )

        Spacer(modifier = Modifier.height(dp11))

        // "새롭게 사용하실 닉네임을 입력해주세요." - 색상 구분
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Error,
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        letterSpacing = (-0.056).sp
                    )
                ) {
                    append("새롭게 사용하실 닉네임")
                }
                withStyle(
                    style = SpanStyle(
                        color = Gray09,
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        letterSpacing = (-0.056).sp
                    )
                ) {
                    append("을 입력해주세요.")
                }
            },
            style = PretendardType.button03Abled
        )

        Spacer(modifier = Modifier.height(dp28))

        // 닉네임 입력 필드
        OMTeamTextField(
            value = nickname,
            onValueChange = { newValue ->
                // 한글, 영어, 숫자만 허용하고 최대 8글자까지
                val filtered = newValue.filter { char ->
                    char.isLetterOrDigit() || char in '가'..'힣'
                }.take(8)
                nickname = filtered
            },
            placeholder = "닉네임을 입력해주세요. (최대 8글자)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                }
        )

        Spacer(modifier = Modifier.height(dp16))

        // 안내 텍스트
        OMTeamText(
            text = "한글, 영어, 숫자를 사용하여 8글자 이내로 닉네임을 설정해주세요.",
            style = PretendardType.body04_1,
            color = Gray09
        )

        Spacer(modifier = Modifier.height(dp56))

        // 변경하기 버튼
        OMTeamButton(
            text = "변경하기",
            onClick = {
                if (nickname.isNotBlank()) {
                    onNicknameChange(nickname)
                    onDismiss()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = nickname.isNotBlank()
        )
    }
}

@Preview
@Composable
private fun ChangeNicknameBottomSheetPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.background(White)
        ) {
            ChangeNicknameBottomSheetContent(
                onDismiss = {},
                onNicknameChange = {},
                onFocusChanged = {}
            )
        }
    }
}