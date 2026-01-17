package com.omteam.designsystem.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.background
import com.omteam.designsystem.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.theme.Error
import com.omteam.designsystem.theme.GreenSub02
import com.omteam.designsystem.theme.GreenSub04
import com.omteam.designsystem.theme.Gray06
import com.omteam.designsystem.theme.PretendardType
import com.omteam.designsystem.theme.Black
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.omt.core.designsystem.R

@Composable
fun OMTeamTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String = "",
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(dp60),
            enabled = enabled,
            textStyle = PretendardType.button03Disabled.copy(
                color = Black
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(Black),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dp60)
                        .background(
                            color = GreenSub02,
                            shape = RoundedCornerShape(dp10)
                        )
                        .border(
                            width = dp1,
                            color = GreenSub04,
                            shape = RoundedCornerShape(dp10)
                        )
                        .padding(horizontal = dp14),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // placeholder 표시
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = PretendardType.button03Disabled,
                            color = Gray06
                        )
                    }
                    innerTextField()
                }
            }
        )

        // 에러 메시지 표시
        if (isError && errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(dp8))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_error),
                    contentDescription = "에러 아이콘",
                    modifier = Modifier.size(dp16)
                )
                Spacer(modifier = Modifier.padding(horizontal = dp4))
                Text(
                    text = errorMessage,
                    style = PretendardType.body02,
                    color = Error
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "OMTeamTextField - 빈 상태")
@Composable
private fun OMTeamTextFieldEmptyPreview() {
    OMTeamTheme {
        var text by remember { mutableStateOf("") }
        OMTeamTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "이름을 입력하세요",
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        )
    }
}

@Preview(showBackground = true, name = "OMTeamTextField - 입력된 상태")
@Composable
private fun OMTeamTextFieldFilledPreview() {
    OMTeamTheme {
        var text by remember { mutableStateOf("테스트") }
        OMTeamTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "이름을 입력하세요",
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        )
    }
}

@Preview(showBackground = true, name = "OMTeamTextField - 에러 상태")
@Composable
private fun OMTeamTextFieldErrorPreview() {
    OMTeamTheme {
        var text by remember { mutableStateOf("닉네임이너무길어요") }
        OMTeamTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "닉네임을 입력해주세요",
            isError = true,
            errorMessage = "글자수를 초과했어요!",
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        )
    }
}

@Preview(showBackground = true, name = "OMTeamTextField - 여러 상태 테스트")
@Composable
private fun OMTeamTextFieldVariantsPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        ) {
            var text1 by remember { mutableStateOf("") }
            OMTeamTextField(
                value = text1,
                onValueChange = { text1 = it },
                placeholder = "이름을 입력하세요",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dp16))

            var text2 by remember { mutableStateOf("") }
            OMTeamTextField(
                value = text2,
                onValueChange = { text2 = it },
                placeholder = "이메일을 입력하세요",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dp16))

            var text3 by remember { mutableStateOf("입력된 텍스트") }
            OMTeamTextField(
                value = text3,
                onValueChange = { text3 = it },
                placeholder = "전화번호를 입력하세요",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dp16))

            var text4 by remember { mutableStateOf("글자수초과된닉네임") }
            OMTeamTextField(
                value = text4,
                onValueChange = { text4 = it },
                placeholder = "닉네임을 입력해주세요",
                isError = true,
                errorMessage = "글자수를 초과했어요!",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}