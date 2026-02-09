package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.DetailedAnalysisNavKey
import com.omteam.impl.tab.DetailedAnalysisScreen
import timber.log.Timber

/**
 * 상세 분석 보기 화면 entry builder
 *
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.detailedAnalysisEntry(
    onBackClick: () -> Unit
) {
    entry<DetailedAnalysisNavKey> {
        Timber.d("## 상세 분석 보기 화면 이동")
        DetailedAnalysisScreen(
            onBackClick = onBackClick
        )
    }
}