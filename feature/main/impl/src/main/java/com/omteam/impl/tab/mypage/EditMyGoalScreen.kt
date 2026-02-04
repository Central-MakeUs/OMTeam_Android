package com.omteam.impl.tab.mypage

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.textfield.OMTeamBorderlessTextField
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R

@Composable
fun EditMyGoalScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var myGoal by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding() // 키보드 올라오면 키보드 높이 맞춰 화면 위로 올림
            .background(White)
            .padding(dp20)
    ) {
        // 상단 헤더
        SubScreenHeader(
            title = stringResource(R.string.edit_my_goal_title),
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(dp50))

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
                    painter = painterResource(id = R.drawable.icon_red_pin),
                    contentDescription = "운동 습관 형성 아이콘",
                    modifier = Modifier.size(dp37)
                )

                Spacer(modifier = Modifier.width(dp12))

                OMTeamBorderlessTextField(
                    placeholder = stringResource(com.omteam.main.impl.R.string.my_page_card),
                    value = myGoal,
                    onValueChange = {
                        myGoal = it
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OMTeamButton(
            text = stringResource(R.string.edit_my_goal_button_text),
            onClick = {
                // TODO : 나의 목표 수정하기 api 호출
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMyGoalScreenPreview() {
    OMTeamTheme {
        EditMyGoalScreen()
    }
}