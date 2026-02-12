package com.omteam.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.datastore.PermissionDataStore
import com.omteam.designsystem.theme.GreenTab
import com.omteam.impl.component.BottomTabBar
import com.omteam.impl.tab.ChatScreen
import com.omteam.impl.tab.HomeScreen
import com.omteam.impl.tab.mypage.MyPageScreen
import com.omteam.impl.tab.ReportScreen
import com.omteam.impl.viewmodel.MainViewModel

/**
 * 하단 탭 네비게이션 포함한 메인 화면
 *
 * 프리뷰에서 PermissionDataStore 처리가 번거로워서 이 화면은 프리뷰 없음
 *
 * @param viewModel 메인 화면 ViewModel
 * @param permissionDataStore 권한 데이터 저장소
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    permissionDataStore: PermissionDataStore,
    onSignOut: () -> Unit = {},
    onNavigateToOther: () -> Unit = {},
    onNavigateToEditMyGoal: (String) -> Unit = {},
    onNavigateToEditMyInfo: () -> Unit = {},
    onNavigateToDetailedAnalysis: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
) {
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()

    MainScreenContent(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        permissionDataStore = permissionDataStore,
        onSignOut = onSignOut,
        onNavigateToOther = onNavigateToOther,
        onNavigateToEditMyGoal = onNavigateToEditMyGoal,
        onNavigateToEditMyInfo = onNavigateToEditMyInfo,
        onNavigateToDetailedAnalysis = onNavigateToDetailedAnalysis,
        onNavigateToChat = onNavigateToChat,
        onTabSelected = { viewModel.selectTab(it) }
    )
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int = 0,
    permissionDataStore: PermissionDataStore,
    onSignOut: () -> Unit = {},
    onNavigateToOther: () -> Unit = {},
    onNavigateToEditMyGoal: (String) -> Unit = {},
    onNavigateToEditMyInfo: () -> Unit = {},
    onNavigateToDetailedAnalysis: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onTabSelected: (Int) -> Unit = {}
) {
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
                0 -> HomeScreen(
                    onNavigateToChat = onNavigateToChat,
                    onNavigateToDetailedAnalysis = onNavigateToDetailedAnalysis
                )
                1 -> ChatScreen()
                2 -> ReportScreen(
                    onNavigateToDetailedAnalysis = onNavigateToDetailedAnalysis
                )
                3 -> MyPageScreen(
                    permissionDataStore = permissionDataStore,
                    onSignOut = onSignOut,
                    onNavigateToOther = onNavigateToOther,
                    onNavigateToEditMyGoal = onNavigateToEditMyGoal,
                    onNavigateToEditMyInfo = onNavigateToEditMyInfo
                )
            }
        }

        BottomTabBar(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected
        )
    }
}