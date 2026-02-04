package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.EditMissionTimeNavKey
import com.omteam.impl.tab.mypage.EditMissionTimeScreen
import timber.log.Timber

/**
 * 미션 투자 시간 수정 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.editMissionTimeEntry(
    onBackClick: () -> Unit
) {
    entry<EditMissionTimeNavKey> {
        Timber.d("## 미션 투자 시간 수정 화면 이동")
        EditMissionTimeScreen(
            onBackClick = onBackClick
        )
    }
}
