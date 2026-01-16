package com.omteam.impl.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.GreenTab
import com.omteam.designsystem.theme.Gray09
import com.omteam.designsystem.theme.Gray05
import com.omteam.omt.core.designsystem.R

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GreenTab)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTabIndex) {
                0 -> HomeContent()
                1 -> ChatContent()
                2 -> ReportContent()
                3 -> MyPageContent()
            }
        }

        BottomTabBar(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )
    }
}

@Composable
private fun BottomTabBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dp60)
            .background(GreenTab),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(
            iconResId = if (selectedTabIndex == 0) R.drawable.home_able else R.drawable.home_disable,
            label = "HOME",
            isSelected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) }
        )

        TabItem(
            iconResId = if (selectedTabIndex == 1) R.drawable.chat_able else R.drawable.chat_disable,
            label = "CHAT",
            isSelected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) }
        )

        TabItem(
            iconResId = if (selectedTabIndex == 2) R.drawable.report_able else R.drawable.report_disable,
            label = "REPORT",
            isSelected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) }
        )

        TabItem(
            iconResId = if (selectedTabIndex == 3) R.drawable.my_page_able else R.drawable.my_page_disable,
            label = "MY PAGE",
            isSelected = selectedTabIndex == 3,
            onClick = { onTabSelected(3) }
        )
    }
}

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
        // 아이콘
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(dp24)
        )

        // 간격
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(dp4))

        // 라벨
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Gray09 else Gray05
        )
    }
}

@Composable
private fun HomeContent() {
    Text("HOME 화면", fontSize = 24.sp, color = Gray09)
}

@Composable
private fun ChatContent() {
    Text("CHAT 화면", fontSize = 24.sp, color = Gray09)
}

@Composable
private fun ReportContent() {
    Text("REPORT 화면", fontSize = 24.sp, color = Gray09)
}

@Composable
private fun MyPageContent() {
    Text("MY PAGE 화면", fontSize = 24.sp, color = Gray09)
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MainScreen()
}