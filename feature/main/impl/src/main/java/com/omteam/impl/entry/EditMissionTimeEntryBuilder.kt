package com.omteam.impl.entry

import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
    entry<EditMissionTimeNavKey> { navKey ->
        Timber.d("## 미션 투자 시간 수정 화면 이동 - initialAvailableTime : ${navKey.initialAvailableTime}")
        
        val context = LocalContext.current
        val onUpdateSuccess = remember {
            {
                Toast.makeText(context, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
                onBackClick()
            }
        }
        
        EditMissionTimeScreen(
            initialAvailableTime = navKey.initialAvailableTime,
            onBackClick = onBackClick,
            onUpdateSuccess = onUpdateSuccess
        )
    }
}