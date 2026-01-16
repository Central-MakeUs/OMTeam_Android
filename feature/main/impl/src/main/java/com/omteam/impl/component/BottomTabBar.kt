package com.omteam.impl.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.dp1
import com.omteam.designsystem.foundation.dp24
import com.omteam.designsystem.foundation.dp4
import com.omteam.designsystem.foundation.dp60
import com.omteam.designsystem.foundation.dp8
import com.omteam.designsystem.theme.Gray09
import com.omteam.designsystem.theme.Gray11
import com.omteam.designsystem.theme.GreenSub03Button
import com.omteam.designsystem.theme.GreenTab
import com.omteam.designsystem.theme.PretendardType
import com.omteam.omt.core.designsystem.R

/**
 * 하단 탭 바 컴포넌트
 * 
 * @param selectedTabIndex 현재 선택된 탭 인덱스 (0~3)
 * @param onTabSelected 탭 선택 시 호출되는 콜백
 */
@Composable
fun BottomTabBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = dp1,
            color = GreenSub03Button
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dp60)
                .background(GreenTab),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabItem(
                iconResId = if (selectedTabIndex == 0) R.drawable.home_able else R.drawable.home_disable,
                label = stringResource(com.omteam.main.impl.R.string.tab_home),
                isSelected = selectedTabIndex == 0,
                onClick = { onTabSelected(0) }
            )

            TabItem(
                iconResId = if (selectedTabIndex == 1) R.drawable.chat_able else R.drawable.chat_disable,
                label = stringResource(com.omteam.main.impl.R.string.tab_chat),
                isSelected = selectedTabIndex == 1,
                onClick = { onTabSelected(1) }
            )

            TabItem(
                iconResId = if (selectedTabIndex == 2) R.drawable.report_able else R.drawable.report_disable,
                label = stringResource(com.omteam.main.impl.R.string.tab_report),
                isSelected = selectedTabIndex == 2,
                onClick = { onTabSelected(2) }
            )

            TabItem(
                iconResId = if (selectedTabIndex == 3) R.drawable.my_page_able else R.drawable.my_page_disable,
                label = stringResource(com.omteam.main.impl.R.string.tab_my_page),
                isSelected = selectedTabIndex == 3,
                onClick = { onTabSelected(3) }
            )
        }
    }
}

/**
 * 개별 탭 아이템
 *
 * @param iconResId 탭 아이콘 리소스 id
 * @param label 영대문자 라벨
 * @param isSelected 탭 선택 여부
 * @param onClick 탭 클릭 시 호출할 콜백
 */
@Composable
private fun TabItem(
    iconResId: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 시 리플 효과 제거
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = dp8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(dp24)
        )

        Spacer(modifier = Modifier.height(dp4))

        OMTeamText(
            text = label,
            style = PretendardType.tabLabel,
            color = if (isSelected) Gray11 else Gray09,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomTabBarPreview() {
    Column {
        BottomTabBar(
            selectedTabIndex = 0,
            onTabSelected = {}
        )
        
        Spacer(modifier = Modifier.height(dp8))
        
        BottomTabBar(
            selectedTabIndex = 2,
            onTabSelected = {}
        )
    }
}