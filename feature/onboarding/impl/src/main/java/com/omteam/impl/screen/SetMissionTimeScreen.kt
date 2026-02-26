package com.omteam.impl.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.R
import com.omteam.impl.screen.component.OnboardingBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetMissionTimeScreen(
    onMissionTimeChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var selectedMissionTime by remember { mutableStateOf("") }
    var customMissionTimeInput by remember { mutableStateOf("") }

    var showBottomSheet by remember { mutableStateOf(false) }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    // confirmValueChange 없이 sheetState를 상위 스코프에서 관리해서 애니메이션이 정상 실행되게 함
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val missionTimeFirstText = stringResource(R.string.mission_time_first)
    val missionTimeSecondText = stringResource(R.string.mission_time_second)
    val missionTimeThirdText = stringResource(R.string.mission_time_third)
    val directInputText = stringResource(R.string.direct_input)

    // "직접 입력하기" 카드에 표시될 텍스트 (커스텀 입력이 있으면 그걸 표시, 없으면 "직접 입력하기")
    val directInputDisplayText = customMissionTimeInput.ifEmpty { directInputText }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OMTeamText(
                    text = stringResource(R.string.set_mission_time_screen_title),
                    style = PaperlogyType.headline02
                )

                Spacer(modifier = Modifier.height(dp20))

                OMTeamCard(
                    text = missionTimeFirstText,
                    isSelected = (selectedMissionTime == missionTimeFirstText),
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedMissionTime = if (selectedMissionTime == missionTimeFirstText) {
                            ""
                        } else {
                            missionTimeFirstText
                        }
                        // 다른 카드 선택 시 커스텀 입력값 초기화
                        customMissionTimeInput = ""
                        onMissionTimeChange(selectedMissionTime)
                    },
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = missionTimeSecondText,
                    isSelected = (selectedMissionTime == missionTimeSecondText),
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedMissionTime = if (selectedMissionTime == missionTimeSecondText) {
                            ""
                        } else {
                            missionTimeSecondText
                        }
                        // 다른 카드 선택 시 커스텀 입력값 초기화
                        customMissionTimeInput = ""
                        onMissionTimeChange(selectedMissionTime)
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = missionTimeThirdText,
                    isSelected = (selectedMissionTime == missionTimeThirdText),
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedMissionTime = if (selectedMissionTime == missionTimeThirdText) {
                            ""
                        } else {
                            missionTimeThirdText
                        }
                        // 다른 카드 선택 시 커스텀 입력값 초기화
                        customMissionTimeInput = ""
                        onMissionTimeChange(selectedMissionTime)
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = directInputDisplayText,
                    isSelected = customMissionTimeInput.isNotEmpty() && selectedMissionTime == customMissionTimeInput,
                    onClick = {
                        showBottomSheet = true
                    }
                )

                // 버튼과 안 겹치게 여백 추가
                Spacer(modifier = Modifier.height(dp20))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dp20),
                horizontalArrangement = Arrangement.Center,
            ) {
                OMTeamButton(
                    text = stringResource(R.string.before),
                    onClick = { onBack() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                Spacer(modifier = Modifier.width(dp9))

                OMTeamButton(
                    text = stringResource(R.string.next),
                    onClick = {
                        if (selectedMissionTime.isNotEmpty()) {
                            onNext()
                        }
                    },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = if (selectedMissionTime.isNotEmpty()) Green07 else Green04,
                    modifier = Modifier.width(dp200)
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            // 스와이프해서 시트가 닫히면 컴포저블도 같이 제거
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            // 시트 자체의 뒤로가기 키 처리를 비활성화해서 BackHandler와 충돌 방지
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false),
            containerColor = White,
            shape = RoundedCornerShape(
                topStart = dp32,
                topEnd = dp32
            )
        ) {
            // 바텀 시트 안에서 뒤로가기 키 단독 처리
            BackHandler(enabled = true) {
                if (isTextFieldFocused) {
                    // TextField에 포커스 있으면 키보드만 닫음
                    focusManager.clearFocus()
                } else {
                    // hide 애니메이션 완료 후 composable 제거해서 window 정리 보장
                    scope.launch {
                        sheetState.hide()
                        showBottomSheet = false
                    }
                }
            }

            Box(modifier = Modifier.imePadding()) {
                OnboardingBottomSheet(
                    placeholder = "최대 30분까지 입력 가능해요.",
                    keyboardType = KeyboardType.Number,
                    inputFilter = { input ->
                        // 숫자만 필터링 + 최대 2자리 제한
                        val digitsOnly = input.filter { it.isDigit() }.take(2)

                        when {
                            digitsOnly.isEmpty() -> ""
                            digitsOnly == "0" -> "" // 0은 입력 불가
                            digitsOnly.length == 1 -> digitsOnly // 1~9는 허용
                            else -> {
                                // 두 자리 숫자는 1~30 범위 안에 드는지 확인
                                val number = digitsOnly.toIntOrNull()
                                if (number != null && number in 1..30) {
                                    digitsOnly
                                } else {
                                    // 범위 초과하면 첫 자리만 유지
                                    digitsOnly.take(1)
                                }
                            }
                        }
                    },
                    onGoalSubmit = { customTime ->
                        val timeValue = customTime.toIntOrNull()
                        if (timeValue != null && timeValue in 1..30) {
                            val displayText = "${customTime}분"
                            customMissionTimeInput = displayText
                            selectedMissionTime = displayText
                            onMissionTimeChange(customTime)
                            // 확인 버튼도 애니메이션 후 제거
                            scope.launch {
                                sheetState.hide()
                                showBottomSheet = false
                            }
                        }
                    },
                    onFocusChanged = { focused ->
                        isTextFieldFocused = focused
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SetMissionTimeScreenPreview() {
    OMTeamTheme {
        SetMissionTimeScreen()
    }
}
