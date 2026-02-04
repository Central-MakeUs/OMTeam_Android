package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.EditMyGoalNavKey
import com.omteam.impl.tab.mypage.EditMyGoalScreen
import timber.log.Timber

/**
 * 나의 목표 수정하기 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.editMyGoalEntry(
    onBackClick: () -> Unit
) {
    entry<EditMyGoalNavKey> {
        Timber.d("## 나의 목표 수정하기 화면 이동")
        EditMyGoalScreen(
            onBackClick = onBackClick
        )
    }
}
