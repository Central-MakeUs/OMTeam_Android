package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.EditPatternNavKey
import com.omteam.impl.tab.mypage.EditPatternScreen
import timber.log.Timber

/**
 * 생활 패턴 수정 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.editPatternEntry(
    onBackClick: () -> Unit
) {
    entry<EditPatternNavKey> { navKey ->
        Timber.d("## 생활 패턴 수정 화면 이동 - initialPattern : ${navKey.initialPattern}")
        EditPatternScreen(
            initialPattern = navKey.initialPattern,
            onBackClick = onBackClick,
            onUpdateSuccess = onBackClick
        )
    }
}