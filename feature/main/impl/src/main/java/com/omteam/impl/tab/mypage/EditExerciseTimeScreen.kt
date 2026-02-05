package com.omteam.impl.tab.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp28
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.White
import com.omteam.impl.component.EditMyInfoItemWithInfo
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R

/**
 * 운동 가능 시간
 */
@Composable
fun EditExerciseTimeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var time by remember { mutableStateOf("") }
    val isValidTime = time.isNotEmpty() && time.toIntOrNull()?.let { it in 1..30 } == true

    Column(
        modifier = modifier
            .fillMaxSize()
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
            label = "운동 가능한 시간대를 입력해 주세요.",
            infoMessage = "몇 시 이후부터 운동할 수 있는지 숫자로 입력해 주세요",
            textFieldValue = time,
            onTextFieldValueChange = { time = it },
            textFieldPlaceholder = "",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Spacer(modifier = Modifier.weight(1f))

        OMTeamButton(
            modifier = Modifier.fillMaxWidth(),
            text = "시간대 수정하기",
            enabled = isValidTime,
            onClick = {
                //
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditExerciseTimeScreenPreview() {
    OMTeamTheme {
        EditExerciseTimeScreen()
    }
}