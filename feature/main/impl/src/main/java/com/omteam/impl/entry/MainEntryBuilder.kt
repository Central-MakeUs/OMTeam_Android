package com.omteam.impl.entry

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.MainNavKey
import com.omteam.impl.screen.MainScreen
import com.omteam.impl.viewmodel.MainViewModel
import timber.log.Timber

/**
 * 메인 화면 entry builder
 * 
 * @param onSignOut 로그아웃 콜백
 */
fun EntryProviderScope<NavKey>.mainEntry(
    onSignOut: () -> Unit
) {
    entry<MainNavKey> {
        Timber.e("## 메인 화면 이동")
        val mainViewModel: MainViewModel = hiltViewModel()
        MainScreen(
            viewModel = mainViewModel,
            onSignOut = {
                // 로그아웃 후 재로그인 시 홈 화면부터 표시되게 인덱스 리셋
                mainViewModel.resetTabIndex()
                onSignOut()
            }
        )
    }
}
