package com.omteam.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R

/**
 * 앱 전체에서 사용되는 에러 바텀 시트
 *
 * @param onHomeClick 홈으로 돌아가기 버튼 클릭 시 호출되는 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorBottomSheet(
    onHomeClick: () -> Unit
) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    ModalBottomSheet(
        onDismissRequest = {
            // 외부 클릭으로 인한 dismiss는 무시
        },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { false } // 모든 상태 변경을 무시해서 바텀 시트 밖을 클릭해도 안 닫히게
        ),
        properties = ModalBottomSheetDefaults.properties(
            shouldDismissOnBackPress = false
        ),
        containerColor = White,
        dragHandle = null,
        shape = RoundedCornerShape(
            topStart = dp32,
            topEnd = dp32
        )
    ) {
        Box(modifier = Modifier.imePadding()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dp20),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(dp44))

                Box(
                    modifier = Modifier
                        .size(dp140)
                        .background(
                            color = ErrorBottomSheetBackground,
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.height(dp40))

                // 일시적 오류가 발생했어요
                OMTeamText(
                    text = stringResource(R.string.error_bottom_sheet_title),
                    style = PaperlogyType.headline01,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(dp16))

                // 잠시 후에 다시 시도해주세요.
                OMTeamText(
                    text = stringResource(R.string.error_bottom_sheet_message),
                    style = PretendardType.button02Disabled.copy(
                        fontSize = 16.sp,
                        letterSpacing = (-0.4).sp
                    ),
                    color = Gray09,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(dp44))

                // 홈으로 돌아가기 버튼
                OMTeamButton(
                    text = stringResource(R.string.error_bottom_sheet_button),
                    onClick = onHomeClick,
                    modifier = Modifier.fillMaxWidth()
                )

                // 하단 네비게이션 바와 안 겹치게 하단 공간 추가
                Spacer(modifier = Modifier.height(dp20 + navigationBarPadding))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorBottomSheetPreview() {
    OMTeamTheme {
        ErrorBottomSheet(
            onHomeClick = {}
        )
    }
}