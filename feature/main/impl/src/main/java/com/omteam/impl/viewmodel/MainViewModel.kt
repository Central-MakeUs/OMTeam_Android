package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * [com.omteam.impl.screen.MainScreen]에서 사용하는 ViewModel
 *
 * 탭 인덱스 관리
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // 현재 선택된 탭의 인덱스 (0: HOME, 1: CHAT, 2: REPORT, 3: MY PAGE)
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    /**
     * 탭 선택
     * 
     * @param index 탭 인덱스 (0: HOME, 1: CHAT, 2: REPORT, 3: MY PAGE)
     */
    fun selectTab(index: Int) {
        if (index in 0..3) {
            Timber.d("## ${index}번 탭 선택")
            _selectedTabIndex.value = index
        } else {
            Timber.e("## 이 탭 인덱스 있는 게 맞음? : $index")
        }
    }

    /**
     * 탭 인덱스를 초기값(0 - HOME)으로 리셋
     *
     * [com.omteam.impl.entry.mainEntry]에서 호출해 로그아웃 후 재로그인 시 홈 화면부터 시작하게 함
     */
    fun resetTabIndex() {
        _selectedTabIndex.value = 0
        Timber.d("## 탭 인덱스 초기화")
    }

}