package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.EditFavoriteExerciseNavKey
import com.omteam.impl.tab.mypage.EditFavoriteExerciseScreen
import timber.log.Timber

/**
 * 선호 운동 수정 화면 entry builder
 * 
 * @param onBackClick 뒤로가기 콜백
 */
fun EntryProviderScope<NavKey>.editFavoriteExerciseEntry(
    onBackClick: () -> Unit
) {
    entry<EditFavoriteExerciseNavKey> { navKey ->
        Timber.d("## 선호 운동 수정 화면 이동 - initialFavoriteExercises : ${navKey.initialFavoriteExercises}")
        EditFavoriteExerciseScreen(
            initialFavoriteExercises = navKey.initialFavoriteExercises,
            onBackClick = onBackClick
        )
    }
}