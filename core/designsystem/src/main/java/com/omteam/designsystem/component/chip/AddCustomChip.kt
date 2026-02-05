package com.omteam.designsystem.component.chip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R

/**
 * "직접 추가하기" Chip 컴포넌트
 * 
 * @param onClick 클릭 이벤트
 * @param modifier Modifier
 * @param isEditing 편집 모드 여부
 * @param value 입력된 값
 * @param onValueChange 값 변경 콜백
 * @param onDone 입력 완료 이벤트 (엔터 키)
 */
@Composable
fun AddCustomChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onDone: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 편집 모드로 전환될 때 포커스 요청
    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier = modifier
            .background(
                color = Gray01,
                shape = RoundedCornerShape(dp32)
            )
            .border(
                width = dp1,
                color = Gray03,
                shape = RoundedCornerShape(dp32)
            )
            .then(
                if (!isEditing) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(horizontal = dp12, vertical = dp8),
        contentAlignment = Alignment.Center
    ) {
        if (isEditing) {
            // 편집 모드면 TextField 표시
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .widthIn(min = dp60)
                    .focusRequester(focusRequester),
                textStyle = PretendardType.button02Disabled.copy(
                    color = Gray12
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onDone()
                    }
                ),
                singleLine = true,
                cursorBrush = SolidColor(Gray12)
            )
        } else {
            // 기본적으론 "직접 추가하기", 아이콘만 표시
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_add),
                    contentDescription = "추가 아이콘"
                )

                Spacer(modifier = Modifier.width(dp8))

                OMTeamText(
                    text = stringResource(R.string.direct_add),
                    style = PretendardType.button02Disabled,
                    color = Gray09
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "기본 상태")
@Composable
private fun AddCustomChipPreview() {
    OMTeamTheme {
        AddCustomChip(onClick = {})
    }
}

@Preview(showBackground = true, name = "편집 모드")
@Composable
private fun AddCustomChipEditingPreview() {
    OMTeamTheme {
        AddCustomChip(
            onClick = {},
            isEditing = true,
            value = "축구"
        )
    }
}