package com.omteam.impl.tab.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.EditMyInfoItemWithInfo
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R

/**
 * 미션에 투자할 수 있는 시간
 */
@Composable
fun EditMissionTimeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    // 숫자만 저장 (1~30)
    var time by remember { mutableStateOf("") }

    // 버튼 활성화 조건 : 1~30 범위의 숫자 입력
    val isValidTime = time.isNotEmpty() && time.toIntOrNull()?.let { it in 1..30 } == true

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .background(White)
            .padding(dp20)
    ) {
        // 상단 헤더
        SubScreenHeader(
            title = stringResource(R.string.edit_my_info_title),
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(dp28))

        EditMyInfoItemWithInfo(
            label = "미션에 투자할 수 있는 시간을 입력해주세요.",
            infoMessage = "최대 30분까지 입력 가능하며, 숫자로 입력해 주세요",
            textFieldValue = time,
            onTextFieldValueChange = { time = it },
            textFieldPlaceholder = "",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            inputFilter = { input ->
                // 숫자만 필터링 + 최대 2자리 제한
                val digitsOnly = input.filter { it.isDigit() }.take(2)

                when {
                    digitsOnly.isEmpty() -> ""
                    digitsOnly == "0" -> "" // 0은 입력 불가
                    digitsOnly.length == 1 -> digitsOnly // 1~9는 허용
                    else -> {
                        // 두 자리 숫자가 1~30 범위 안에 포함되는지 확인
                        val number = digitsOnly.toIntOrNull()
                        if (number != null && number in 1..30) {
                            digitsOnly
                        } else {
                            // 범위 초과하면 첫 자리만 유지
                            digitsOnly.take(1)
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        OMTeamButton(
            modifier = Modifier.fillMaxWidth(),
            text = "미션 투자 시간 수정하기",
            enabled = isValidTime,
            onClick = {
                //
            }
        )
    }
}

@Preview
@Composable
private fun EditMissionTimeScreenPreview() {
    OMTeamTheme {
        EditMissionTimeScreen()
    }
}