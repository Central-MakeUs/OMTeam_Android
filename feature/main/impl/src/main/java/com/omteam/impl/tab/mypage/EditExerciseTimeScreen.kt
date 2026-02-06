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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.chip.SelectableInfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.EditMyInfoItemWithInfo
import com.omteam.impl.component.SubScreenHeader
import com.omteam.impl.viewmodel.MyPageViewModel
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import com.omteam.omt.core.designsystem.R

/**
 * 운동 가능 시간
 */
@Composable
fun EditExerciseTimeScreen(
    modifier: Modifier = Modifier,
    initialExerciseTime: String = "",
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onUpdateSuccess: () -> Unit = {}
) {
    val onboardingInfoState by myPageViewModel.onboardingInfoState.collectAsStateWithLifecycle()

    // 수정 성공 시 뒤로 가기
    LaunchedEffect(onboardingInfoState) {
        if (onboardingInfoState is MyPageOnboardingState.Success) {
            onUpdateSuccess()
        }
    }

    EditExerciseTimeContent(
        modifier = modifier,
        initialExerciseTime = initialExerciseTime,
        isLoading = onboardingInfoState is MyPageOnboardingState.Loading,
        onBackClick = onBackClick,
        onUpdateClick = { startTime, endTime ->
            myPageViewModel.updateAvailableTime(startTime, endTime)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditExerciseTimeContent(
    modifier: Modifier = Modifier,
    initialExerciseTime: String = "",
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onUpdateClick: (String, String) -> Unit = { _, _ -> }
) {
    var selectedTime by remember(initialExerciseTime) { mutableStateOf(initialExerciseTime) }

    val before18 = stringResource(R.string.before_18)
    val after18 = stringResource(R.string.after_18)
    val after19 = stringResource(R.string.after_19)
    val after20 = stringResource(R.string.after_20)
    
    val availableTimes = listOf(before18, after18, after19, after20)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = dp20)
        ) {
            Spacer(modifier = Modifier.height(dp20))

            SubScreenHeader(
                title = stringResource(R.string.edit_my_info_title),
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(dp28))

            EditMyInfoItemWithInfo(
                label = stringResource(R.string.choose_available_hours),
                infoMessage = stringResource(R.string.choose_available_hours_info),
                chips = if (selectedTime.isNotEmpty()) listOf(selectedTime) else emptyList(),
                onClick = {}
            )

            Spacer(modifier = Modifier.height(dp52))

            OMTeamText(
                text = stringResource(R.string.available_hours_list),
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
                                ""
                            } else {
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
            text = stringResource(R.string.edit_exercise_time_button),
            enabled = selectedTime.isNotEmpty() && !isLoading,
            onClick = {
                val (startTime, endTime) = when (selectedTime) {
                    before18 -> "00:00" to "17:59"
                    after18 -> "18:00" to "23:59"
                    after19 -> "19:00" to "23:59"
                    after20 -> "20:00" to "23:59"
                    else -> "18:00" to "23:59"
                }
                onUpdateClick(startTime, endTime)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditExerciseTimeScreenPreview() {
    OMTeamTheme {
        EditExerciseTimeContent()
    }
}

@Preview(showBackground = true, name = "시간대 선택된 상태")
@Composable
private fun EditExerciseTimeScreenWithSelectionPreview() {
    OMTeamTheme {
        EditExerciseTimeContent(initialExerciseTime = "19:00 이후부터")
    }
}