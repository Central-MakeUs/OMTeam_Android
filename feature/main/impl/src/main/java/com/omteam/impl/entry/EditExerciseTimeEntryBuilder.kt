package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.EditExerciseTimeNavKey
import com.omteam.impl.tab.mypage.EditExerciseTimeScreen
import timber.log.Timber

/**
 * 운동 가능 시간 수정 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.editExerciseTimeEntry(
    onBackClick: () -> Unit
) {
    entry<EditExerciseTimeNavKey> {
        Timber.d("## 운동 가능 시간 수정 화면 이동")
        EditExerciseTimeScreen(
            onBackClick = onBackClick
        )
    }
}
