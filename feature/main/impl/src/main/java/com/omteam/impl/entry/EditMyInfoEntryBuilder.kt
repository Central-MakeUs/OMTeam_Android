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
 * @param onNavigateToEditExerciseTime 운동 가능 시간 수정 화면 이동 콜백
 * @param onNavigateToEditMissionTime 미션 투자 시간 수정 화면 이동 콜백
 * @param onNavigateToEditFavoriteExercise 선호 운동 수정 화면 이동 콜백
 * @param onNavigateToEditPattern 생활 패턴 수정 화면 이동 콜백
 */
fun EntryProviderScope<NavKey>.editMyInfoEntry(
    onBackClick: () -> Unit,
    onNavigateToEditExerciseTime: (String) -> Unit,
    onNavigateToEditMissionTime: (String) -> Unit,
    onNavigateToEditFavoriteExercise: (List<String>) -> Unit,
    onNavigateToEditPattern: (String) -> Unit
) {
    entry<EditMyInfoNavKey> {
        Timber.d("## 내 정보 수정하기 화면 이동")
        EditMyInfoScreen(
            onBackClick = onBackClick,
            onNavigateToEditExerciseTime = onNavigateToEditExerciseTime,
            onNavigateToEditMissionTime = onNavigateToEditMissionTime,
            onNavigateToEditFavoriteExercise = onNavigateToEditFavoriteExercise,
            onNavigateToEditPattern = onNavigateToEditPattern
        )
    }
}
