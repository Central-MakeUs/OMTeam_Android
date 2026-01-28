package com.omteam.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.theme.GreenTab
import com.omteam.impl.component.BottomTabBar
import com.omteam.impl.tab.ChatScreen
import com.omteam.impl.tab.HomeScreen
import com.omteam.impl.tab.MyPageScreen
import com.omteam.impl.tab.ReportScreen
import com.omteam.impl.viewmodel.MainViewModel

/**
 * 하단 탭 네비게이션 포함한 메인 화면
 *
 * [MainViewModel] 통해 탭 상태 관리
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {}
) {
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GreenTab)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedTabIndex) {
                0 -> HomeScreen(viewModel = viewModel)
                1 -> ChatScreen()
                2 -> ReportScreen()
                3 -> MyPageScreen(onSignOut = onSignOut)
            }
        }

        BottomTabBar(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { viewModel.selectTab(it) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MainScreen()
}