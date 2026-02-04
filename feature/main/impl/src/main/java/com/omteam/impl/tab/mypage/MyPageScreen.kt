package com.omteam.impl.tab.mypage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.ChangeNicknameBottomSheetContent
import com.omteam.omt.core.designsystem.R
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {},
    onNavigateToOther: () -> Unit = {},
    onNavigateToEditMyGoal: () -> Unit = {}
) {
    var showChangeNicknameBottomSheet by remember { mutableStateOf(false) }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(dp20)
            .verticalScroll(rememberScrollState()),
    ) {
        Image(
            painter = painterResource(id = R.drawable.screen_inner_logo),
            contentDescription = "왼쪽 상단 로고",
            modifier = Modifier.size(dp50)
        )

        Spacer(modifier = Modifier.height(dp32))

        // 프로필 이미지 + 편집 아이콘
        Box(
            modifier = Modifier
                .size(dp140)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .size(dp140)
                    .background(
                        color = ErrorBottomSheetBackground,
                        shape = CircleShape
                    )
            )

            // 편집 아이콘
            Image(
                painter = painterResource(id = R.drawable.mypage_edit_able),
                contentDescription = "닉네임 수정",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = dp8)
                    .size(dp36)
                    .clickable(
                        // 물결 효과 제거
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showChangeNicknameBottomSheet = true
                    }
            )
        }

        Spacer(modifier = Modifier.height(dp16))

        OMTeamText(
            text = "닉네임",
            style = PaperlogyType.headline03,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(dp53))

        // 나의 목표, 수정하기
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OMTeamText(
                text = "나의 목표",
                style = PaperlogyType.headline04
            )

            Spacer(modifier = Modifier.weight(1f))

            // 수정하기
            Box(
                modifier = Modifier
                    .height(dp32)
                    .background(
                        color = Green07,
                        shape = RoundedCornerShape(dp4)
                    )
                    .padding(horizontal = dp10)
                    .clickable(
                        // 물결 효과 제거
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        Timber.d("## 수정하기 클릭")
                    },
                contentAlignment = Alignment.Center
            ) {
                OMTeamText(
                    text = stringResource(com.omteam.main.impl.R.string.edit_button),
                    style = PretendardType.button03Abled,
                    color = Black02,
                    textAlign = TextAlign.Center,
                    onClick = onNavigateToEditMyGoal
                )
            }
        }

        Spacer(modifier = Modifier.height(dp10))

        // 운동 습관 형성 카드
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dp91)
                .background(
                    color = Gray02,
                    shape = RoundedCornerShape(dp10)
                )
                .padding(horizontal = dp12, vertical = dp27)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_exercise),
                    contentDescription = "운동 습관 형성 아이콘",
                    modifier = Modifier.size(dp37)
                )

                Spacer(modifier = Modifier.width(dp12))

                OMTeamText(
                    text = stringResource(com.omteam.main.impl.R.string.my_page_card),
                    style = PaperlogyType.headline03,
                    color = Gray11
                )
            }
        }

        Spacer(modifier = Modifier.height(dp50))

        MyPageMenuItemWithSwitch(
            text = stringResource(com.omteam.main.impl.R.string.setting_alarm_title)
        )
        MyPageMenuDivider()
        
        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.edit_my_info),
            onClick = { Timber.d("## 내 정보 수정하기 클릭") }
        )
        MyPageMenuDivider()
        
        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.inquiry),
            onClick = { Timber.d("## 문의하기 클릭") }
        )
        MyPageMenuDivider()
        
        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.other),
            onClick = onNavigateToOther,
        )
        MyPageMenuDivider()

        MyPageMenuItem(
            text = stringResource(com.omteam.main.impl.R.string.logout),
            onClick = onSignOut,
            showDivider = false
        )
    }

    // 닉네임 변경 바텀 시트
    if (showChangeNicknameBottomSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()

        // 바텀 시트보다 먼저 BackHandler 등록해서 키보드 표시될 때 뒤로가기 클릭 시 바텀 시트가 사라지지 않게
        BackHandler(enabled = true) {
            if (isTextFieldFocused) {
                // TextField에 포커스가 있으면 키보드만 닫음
                focusManager.clearFocus()
            } else {
                // 포커스가 없으면 바텀시트 닫음
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showChangeNicknameBottomSheet = false
                    }
                }
            }
        }

        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showChangeNicknameBottomSheet = false
                    }
                }
            },
            sheetState = sheetState,
            containerColor = White,
            shape = RoundedCornerShape(
                topStart = dp32,
                topEnd = dp32
            ),
            properties = ModalBottomSheetProperties(
                securePolicy = SecureFlagPolicy.Inherit,
                shouldDismissOnBackPress = false
            )
        ) {
            Box(modifier = Modifier.imePadding()) {
                ChangeNicknameBottomSheetContent(
                    onDismiss = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showChangeNicknameBottomSheet = false
                            }
                        }
                    },
                    onNicknameChange = { newNickname ->
                        Timber.d("## 닉네임 변경 : $newNickname")
                        // TODO: 닉네임 변경 API 호출
                    },
                    onFocusChanged = { focused ->
                        isTextFieldFocused = focused
                    }
                )
            }
        }
    }
}

// 커스텀 스위치를 포함하는 알림 설정하기 영역 표현
@Composable
private fun MyPageMenuItemWithSwitch(
    text: String
) {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                // 물결 효과 제거
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isChecked = !isChecked
                Timber.d("## 알림 설정하기 클릭 : $isChecked")
            }
            .padding(end = dp32),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OMTeamText(
            text = text,
            style = PretendardType.body02_2,
            color = Gray11
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomSwitch(
            checked = isChecked
        )
    }
    
    Spacer(modifier = Modifier.height(dp18))
}

@Composable
private fun CustomSwitch(
    checked: Boolean
) {
    // material3의 스위치는 off 상태의 thumb가 on 상태의 thumb보다 크기가 작음
    // Box로 커스텀 스위치를 구현해 thumb 크기 통일 + 스프링 애니메이션 적용해 최대한 material3 스위치와 비슷하게 구현
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) dp17 else dp0,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (checked) Green07 else Gray03,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
    )

    // 스위치 배경
    Box(
        modifier = Modifier
            .width(dp43)
            .height(dp24)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(dp12)
            )
            .padding(horizontal = dp3, vertical = dp2),
        contentAlignment = Alignment.CenterStart
    ) {
        // 스위치 thumb (온오프 모두 해당)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(dp20)
                .background(
                    color = White,
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun MyPageMenuItem(
    text: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        OMTeamText(
            text = text,
            style = PretendardType.body02_2,
            color = Gray11,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    // 물결 효과 제거
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
        )

        if (showDivider) {
            Spacer(modifier = Modifier.height(dp18))
        }
    }
}

@Composable
private fun MyPageMenuDivider() {
    Spacer(
        modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth()
            .background(Gray03)
    )
    Spacer(modifier = Modifier.height(dp18))
}

@Preview(showBackground = true)
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen()
}