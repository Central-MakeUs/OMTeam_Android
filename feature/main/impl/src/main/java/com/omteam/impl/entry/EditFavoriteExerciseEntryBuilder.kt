package com.omteam.impl.entry

import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
        
        val context = LocalContext.current
        val onUpdateSuccess = remember {
            {
                Toast.makeText(context, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
                onBackClick()
            }
        }
        
        EditFavoriteExerciseScreen(
            initialFavoriteExercises = navKey.initialFavoriteExercises,
            onBackClick = onBackClick,
            onUpdateSuccess = onUpdateSuccess
        )
    }
}