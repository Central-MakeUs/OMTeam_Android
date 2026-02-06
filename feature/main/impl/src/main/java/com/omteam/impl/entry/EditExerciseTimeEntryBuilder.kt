package com.omteam.impl.entry

import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
    entry<EditExerciseTimeNavKey> { navKey ->
        Timber.d("## 운동 가능 시간 수정 화면 이동 - initialExerciseTime : ${navKey.initialExerciseTime}")
        
        val context = LocalContext.current
        val onUpdateSuccess = remember {
            {
                Toast.makeText(context, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
                onBackClick()
            }
        }
        
        EditExerciseTimeScreen(
            initialExerciseTime = navKey.initialExerciseTime,
            onBackClick = onBackClick,
            onUpdateSuccess = onUpdateSuccess
        )
    }
}