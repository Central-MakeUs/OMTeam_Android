package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.WebViewNavKey
import com.omteam.impl.screen.WebViewScreen
import timber.log.Timber

/**
 * 웹뷰 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.webViewEntry(
    onBackClick: () -> Unit
) {
    entry<WebViewNavKey> { navKey ->
        Timber.d("## 웹뷰 화면 이동 : ${navKey.url}")
        WebViewScreen(
            url = navKey.url,
            onBackClick = onBackClick
        )
    }
}