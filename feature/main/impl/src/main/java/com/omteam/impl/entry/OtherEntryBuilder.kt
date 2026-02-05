package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.OtherNavKey
import com.omteam.impl.tab.mypage.OtherScreen
import timber.log.Timber

/**
 * 기타 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 * @param onNavigateToWebView 웹뷰 화면 이동 콜백
 */
fun EntryProviderScope<NavKey>.otherEntry(
    onBackClick: () -> Unit,
    onNavigateToWebView: (url: String) -> Unit
) {
    entry<OtherNavKey> {
        Timber.d("## 기타 화면 이동")
        OtherScreen(
            onBackClick = onBackClick,
            onNavigateToWebView = onNavigateToWebView
        )
    }
}
