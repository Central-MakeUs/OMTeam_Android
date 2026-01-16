package com.omteam.impl.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
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
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.PaperlogyType
import com.omteam.designsystem.theme.White
import com.omteam.impl.screen.component.OnboardingBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalOnboardingScreen(
    initialGoal: String = "",
    onGoalChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf(initialGoal) }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OMTeamText(
                text = "OMT에서 함께 이루고 싶은 목표를\n선택해주세요.",
                style = PaperlogyType.headline02
            )

            Spacer(modifier = Modifier.height(dp20))

            OMTeamCard(
                text = "체중 감량하기",
                isSelected = (selectedGoal == "체중 감량하기"),
                textStyle = PaperlogyType.onboardingCardText,
                onClick = {
                    // 같은 카드 짝수 회 클릭 시 선택 해제
                    selectedGoal = if (selectedGoal == "체중 감량하기") {
                        ""
                    } else {
                        "체중 감량하기"
                    }
                    onGoalChange(selectedGoal)
                },
            )

            Spacer(modifier = Modifier.height(dp12))

            OMTeamCard(
                text = "운동 습관 형성하기",
                isSelected = (selectedGoal == "운동 습관 형성하기"),
                textStyle = PaperlogyType.onboardingCardText,
                onClick = {
                    // 같은 카드 짝수 회 클릭 시 선택 해제
                    selectedGoal = if (selectedGoal == "운동 습관 형성하기") {
                        ""
                    } else {
                        "운동 습관 형성하기"
                    }
                    onGoalChange(selectedGoal)
                }
            )

            Spacer(modifier = Modifier.height(dp12))

            OMTeamCard(
                text = "직접 입력하기",
                isSelected = (selectedGoal == "직접 입력하기"),
                textStyle = PaperlogyType.onboardingCardText,
                onClick = {
                    showBottomSheet = true
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth()
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
                // 외부 클릭으로 인한 dismiss는 무시
            },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
                confirmValueChange = { false } // 모든 상태 변경을 무시해서 바텀 시트 바깥 클릭으로 닫히지 않음
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
                    placeholder = "목표를 입력해 주세요.",
                    onGoalSubmit = { customGoal ->
                        selectedGoal = customGoal
                        onGoalChange(customGoal)
                        showBottomSheet = false
                    },
                    onFocusChanged = { focused ->
                        isTextFieldFocused = focused
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun GoalOnboardingScreenPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            GoalOnboardingScreen()
        }
    }
}