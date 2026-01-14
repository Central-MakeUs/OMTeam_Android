package com.omteam.impl.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.textfield.OMTeamTextField
import com.omteam.designsystem.foundation.dp10
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp260
import com.omteam.designsystem.theme.OMTeamTheme

@Composable
fun OnboardingBottomSheet(
    onGoalSubmit: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {}
) {
    var goalText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(dp260)
            .padding(horizontal = dp20)
            .padding(bottom = dp20),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dp10))

        // 텍스트 입력 필드
        OMTeamTextField(
            value = goalText,
            onValueChange = { goalText = it },
            placeholder = "예: 건강한 식습관 만들기",
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                }
        )

        Spacer(modifier = Modifier.weight(1f))

        // 확인 버튼
        OMTeamButton(
            text = "확인",
            onClick = {
                if (goalText.isNotBlank()) {
                    onGoalSubmit(goalText)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = goalText.isNotBlank()
        )
    }
}

@Preview
@Composable
private fun OnboardingBottomSheetPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            OnboardingBottomSheet(
                onGoalSubmit = {}
            )
        }
    }
}