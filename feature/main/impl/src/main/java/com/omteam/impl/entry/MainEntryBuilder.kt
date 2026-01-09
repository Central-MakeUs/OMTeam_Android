package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.MainNavKey
import com.omteam.impl.screen.MainScreen
import timber.log.Timber

/**
 * 메인 화면 entry builder
 */
fun EntryProviderScope<NavKey>.mainEntry() {
    entry<MainNavKey> {
        Timber.e("## 메인 화면 이동")
        MainScreen()
    }
}
