package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.EditMyInfoNavKey
import com.omteam.impl.tab.mypage.EditMyInfoScreen
import timber.log.Timber

/**
 * 내 정보 수정하기 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.editMyInfoEntry(
    onBackClick: () -> Unit
) {
    entry<EditMyInfoNavKey> {
        Timber.d("## 내 정보 수정하기 화면 이동")
        EditMyInfoScreen(
            onBackClick = onBackClick
        )
    }
}
