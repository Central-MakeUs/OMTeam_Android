package com.omteam.impl.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.chip.InfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.component.textfield.OMTeamBorderlessTextField
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R

/**
 * 내 정보 수정 화면의 정보 아이템 (알림 문구 포함)
 * 
 * @param label 라벨 텍스트 (예: "운동 가능 시간")
 * @param infoMessage 하단에 표시할 알림 문구
 * @param modifier Modifier
 * @param chips Chip으로 표시할 값들 (선호 운동용, 최대 3개)
 * @param textFieldValue TextField로 표시할 값
 * @param onTextFieldValueChange TextField 값 변경 콜백
 * @param textFieldPlaceholder TextField placeholder
 * @param keyboardOptions 키보드 옵션
 * @param inputFilter 입력 값을 필터링하는 함수
 * @param onClick 클릭 이벤트
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditMyInfoItemWithInfo(
    label: String,
    infoMessage: String,
    modifier: Modifier = Modifier,
    chips: List<String> = emptyList(),
    textFieldValue: String = "",
    onTextFieldValueChange: (String) -> Unit = {},
    textFieldPlaceholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    inputFilter: ((String) -> String)? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        // 라벨과 화살표
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OMTeamText(
                text = label,
                style = PretendardType.body03_1,
                color = Gray07
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.icon_arrow_forth),
                contentDescription = "오른쪽 화살",
            )
        }

        Spacer(modifier = Modifier.height(dp18))

        if (chips.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(start = dp4),
                horizontalArrangement = Arrangement.spacedBy(dp8),
                verticalArrangement = Arrangement.spacedBy(dp8)
            ) {
                chips.forEach { chipText ->
                    InfoChip(text = chipText)
                }
            }
            
            Spacer(modifier = Modifier.height(dp10))
            
            // 구분선
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dp1)
                    .background(Gray05)
            )

            Spacer(modifier = Modifier.height(dp10))
        } else {
            OMTeamBorderlessTextField(
                value = textFieldValue,
                onValueChange = onTextFieldValueChange,
                placeholder = textFieldPlaceholder,
                textStyle = PretendardType.button02Enabled,
                keyboardOptions = keyboardOptions,
                inputFilter = inputFilter
            )

            Spacer(modifier = Modifier.height(dp8))

            // 구분선
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dp1)
                    .background(Gray05)
            )

            Spacer(modifier = Modifier.height(dp10))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dp5),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_info),
                contentDescription = "정보 아이콘",
                modifier = Modifier.size(dp16)
            )

            Spacer(modifier = Modifier.width(dp8))

            OMTeamText(
                text = infoMessage,
                style = PretendardType.body04_2,
                color = Gray07
            )
        }
    }
}

@Preview(showBackground = true, name = "EditMyInfoItemWithInfo - Chip")
@Composable
private fun EditMyInfoItemWithInfoChipPreview() {
    OMTeamTheme {
        EditMyInfoItemWithInfo(
            label = "선호 운동",
            infoMessage = "최대 3개까지 선택할 수 있어요",
            chips = listOf("헬스", "요가", "필라테스")
        )
    }
}

@Preview(showBackground = true, name = "EditMyInfoItemWithInfo - TextField")
@Composable
private fun EditMyInfoItemWithInfoTextFieldPreview() {
    OMTeamTheme {
        var text by remember { mutableStateOf("") }
        EditMyInfoItemWithInfo(
            label = "닉네임",
            infoMessage = "한글, 영문, 숫자만 사용 가능해요",
            textFieldValue = text,
            onTextFieldValueChange = { text = it },
            textFieldPlaceholder = "닉네임을 입력하세요"
        )
    }
}
