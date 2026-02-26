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
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
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
fun SetGoalScreen(
    initialGoal: String = "",
    onGoalChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf(initialGoal) }
    var customGoalInput by remember { mutableStateOf("") }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    // confirmValueChange 없이 sheetState를 상위 스코프에서 관리해서 애니메이션이 정상 실행되게 함
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val goalTitleText = stringResource(R.string.set_goal_screen_title)
    val dietText = stringResource(R.string.diet)
    val habitFormationText = stringResource(R.string.habit_formation)
    val directInputText = stringResource(R.string.direct_input)

    // "직접 입력하기" 카드에 표시될 텍스트 (커스텀 입력이 있으면 그걸 표시, 없으면 "직접 입력하기")
    val directInputDisplayText = customGoalInput.ifEmpty { directInputText }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OMTeamText(
                text = goalTitleText,
                style = PaperlogyType.headline02
            )

            Spacer(modifier = Modifier.height(dp20))

            OMTeamCard(
                text = dietText,
                isSelected = (selectedGoal == dietText),
                onClick = {
                    // 같은 카드 짝수 회 클릭 시 선택 해제
                    selectedGoal = if (selectedGoal == dietText) {
                        ""
                    } else {
                        dietText
                    }
                    // 다른 카드 선택 시 커스텀 입력값 초기화
                    customGoalInput = ""
                    onGoalChange(selectedGoal)
                },
            )

            Spacer(modifier = Modifier.height(dp12))

            OMTeamCard(
                text = habitFormationText,
                isSelected = (selectedGoal == habitFormationText),
                onClick = {
                    // 같은 카드 짝수 회 클릭 시 선택 해제
                    selectedGoal = if (selectedGoal == habitFormationText) {
                        ""
                    } else {
                        habitFormationText
                    }
                    // 다른 카드 선택 시 커스텀 입력값 초기화
                    customGoalInput = ""
                    onGoalChange(selectedGoal)
                }
            )

            Spacer(modifier = Modifier.height(dp12))

            OMTeamCard(
                text = directInputDisplayText,
                isSelected = customGoalInput.isNotEmpty() && selectedGoal == customGoalInput,
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
                        if (selectedGoal.isNotEmpty()) {
                            onNext()
                        }
                    },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = if (selectedGoal.isNotEmpty()) Green07 else Green04,
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
                    placeholder = stringResource(R.string.direct_input_placeholder),
                    onGoalSubmit = { customGoal ->
                        customGoalInput = customGoal
                        selectedGoal = customGoal
                        onGoalChange(customGoal)
                        // 확인 버튼도 애니메이션 후 제거
                        scope.launch {
                            sheetState.hide()
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

@Preview
@Composable
private fun SetGoalScreenPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            SetGoalScreen()
        }
    }
}
