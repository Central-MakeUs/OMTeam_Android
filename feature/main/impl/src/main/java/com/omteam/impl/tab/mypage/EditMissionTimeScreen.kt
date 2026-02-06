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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.EditMyInfoItemWithInfo
import com.omteam.impl.component.SubScreenHeader
import com.omteam.impl.viewmodel.MyPageViewModel
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import com.omteam.omt.core.designsystem.R

/**
 * 미션에 투자할 수 있는 시간
 */
@Composable
fun EditMissionTimeScreen(
    modifier: Modifier = Modifier,
    initialAvailableTime: String = "", // 이전 화면에서 온보딩 정보로 가져온 시간 값
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onUpdateSuccess: () -> Unit = {}
) {
    // 숫자만 저장 (1~30)
    // remember(key)로 initialAvailableTime이 변경되면 자동으로 상태 업데이트
    var time by remember(initialAvailableTime) { mutableStateOf(initialAvailableTime) }

    // 버튼 활성화 조건 : 1~30 범위의 숫자 입력
    val isValidTime = time.isNotEmpty() && time.toIntOrNull()?.let { it in 1..30 } == true
    
    val onboardingInfoState by myPageViewModel.onboardingInfoState.collectAsStateWithLifecycle()

    // 수정 성공 시 뒤로 가기
    LaunchedEffect(onboardingInfoState) {
        if (onboardingInfoState is MyPageOnboardingState.Success) {
            onUpdateSuccess()
        }
    }

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
            label = stringResource(R.string.input_available_mission_hours),
            infoMessage = stringResource(R.string.input_available_mission_hours_info),
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
            text = stringResource(R.string.edit_available_mission_button),
            enabled = isValidTime && onboardingInfoState !is MyPageOnboardingState.Loading,
            onClick = {
                val minutes = time.toIntOrNull()
                if (minutes != null) {
                    myPageViewModel.updateMinExerciseMinutes(minutes)
                }
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