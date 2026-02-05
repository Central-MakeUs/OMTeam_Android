package com.omteam.impl.tab.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.chip.SelectableInfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp28
import com.omteam.designsystem.foundation.dp52
import com.omteam.designsystem.foundation.dp8
import com.omteam.designsystem.theme.Gray10
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.PretendardType
import com.omteam.designsystem.theme.White
import com.omteam.impl.component.EditMyInfoItemWithInfo
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R

/**
 * 운동 가능 시간
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditExerciseTimeScreen(
    modifier: Modifier = Modifier,
    initialExerciseTime: String = "", // 이전 화면에서 온보딩 정보로 가져온 시간대 값
    onBackClick: () -> Unit = {}
) {
    // 선택된 운동 시간대
    var selectedTime by remember { mutableStateOf(initialExerciseTime) }

    // 선택 가능한 시간대 목록
    val availableTimes = listOf(
        "18:00 이전부터", "18:00 이후부터", "19:00 이후부터", "20:00 이후부터",
    )
    
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 스크롤 가능한 영역
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = dp20)
        ) {
            Spacer(modifier = Modifier.height(dp20))

            // 상단 헤더
            SubScreenHeader(
                title = stringResource(R.string.edit_my_info_title),
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(dp28))

            // 선택된 시간대 표시
            EditMyInfoItemWithInfo(
                label = "운동 가능한 시간대를 선택해 주세요.",
                infoMessage = "몇 시부터 운동할 수 있는지 선택해주세요",
                chips = if (selectedTime.isNotEmpty()) listOf(selectedTime) else emptyList(),
                onClick = {}
            )

            Spacer(modifier = Modifier.height(dp52))

            OMTeamText(
                text = "운동 가능 시간대 목록",
                style = PretendardType.button03Abled,
                color = Gray10
            )

            Spacer(modifier = Modifier.height(dp20))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dp8),
                verticalArrangement = Arrangement.spacedBy(dp8)
            ) {
                availableTimes.forEach { time ->
                    SelectableInfoChip(
                        text = time,
                        isSelected = selectedTime == time,
                        onClick = {
                            selectedTime = if (selectedTime == time) {
                                // 이미 선택됐으면 선택 해제
                                ""
                            } else {
                                // 새 시간대 선택
                                time
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(dp20))
        }

        OMTeamButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp20)
                .padding(bottom = dp20),
            text = "시간대 수정하기",
            enabled = selectedTime.isNotEmpty(),
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

@Preview(showBackground = true, name = "시간대 선택된 상태")
@Composable
private fun EditExerciseTimeScreenWithSelectionPreview() {
    OMTeamTheme {
        EditExerciseTimeScreen(initialExerciseTime = "19:00 이후부터")
    }
}