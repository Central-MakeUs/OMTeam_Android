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
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.dp12
import com.omteam.designsystem.foundation.dp126
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp200
import com.omteam.designsystem.foundation.dp32
import com.omteam.designsystem.foundation.dp60
import com.omteam.designsystem.foundation.dp8
import com.omteam.designsystem.foundation.dp9
import com.omteam.designsystem.theme.GreenSub03Button
import com.omteam.designsystem.theme.GreenSub07Button
import com.omteam.designsystem.theme.PaperlogyType
import com.omteam.designsystem.theme.White
import com.omteam.impl.screen.component.OnboardingBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetMissionTimeScreen(
    onMissionTimeChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var selectedMissionTime by remember { mutableStateOf("") }

    var showBottomSheet by remember { mutableStateOf(false) }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

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
                    text = "OMT와 함께 미션 수행에\n투자할 수 있는 시간을 알려주세요!",
                    style = PaperlogyType.headline02
                )

                Spacer(modifier = Modifier.height(dp20))

                OMTeamCard(
                    text = "05분",
                    isSelected = (selectedMissionTime == "05분"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedMissionTime = if (selectedMissionTime == "05분") {
                            ""
                        } else {
                            "05분"
                        }
                        onMissionTimeChange(selectedMissionTime)
                    },
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = "10분",
                    isSelected = (selectedMissionTime == "10분"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedMissionTime = if (selectedMissionTime == "10분") {
                            ""
                        } else {
                            "10분"
                        }
                        onMissionTimeChange(selectedMissionTime)
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = "15분",
                    isSelected = (selectedMissionTime == "15분"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedMissionTime = if (selectedMissionTime == "15분") {
                            ""
                        } else {
                            "15분"
                        }
                        onMissionTimeChange(selectedMissionTime)
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = "직접 입력하기",
                    isSelected = (selectedMissionTime == "직접 입력하기"),
                    textStyle = PaperlogyType.onboardingCardText,
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
                    text = "이전",
                    onClick = { onBack() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                Spacer(modifier = Modifier.width(dp9))

                OMTeamButton(
                    text = "다음",
                    onClick = { onNext() },
                    height = dp60,
                    cornerRadius = dp8,
                    modifier = Modifier.width(dp200)
                )
            }
        }
    }

    if (showBottomSheet) {
        // 바텀 시트보다 먼저 BackHandler 등록해서 키보드 표시될 때 뒤로가기 클릭 시 바텀 시트가 사라지지 않게
        BackHandler(enabled = true) {
            if (isTextFieldFocused) {
                // TextField에 포커스 있으면 키보드만 닫음
                focusManager.clearFocus()
            } else {
                // 포커스가 없으면 바텀시트 닫음
                showBottomSheet = false
            }
        }

        ModalBottomSheet(
            onDismissRequest = {
                if (!isTextFieldFocused) {
                    showBottomSheet = false
                }
            },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            ),
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false
            ),
            containerColor = White,
            shape = RoundedCornerShape(
                topStart = dp32,
                topEnd = dp32
            )
        ) {
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
                            selectedMissionTime = "${customTime}분"
                            onMissionTimeChange(customTime)
                            showBottomSheet = false
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