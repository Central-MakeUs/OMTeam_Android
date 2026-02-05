package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.usecase.GetOnboardingInfoUseCase
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getOnboardingInfoUseCase: GetOnboardingInfoUseCase
): ViewModel() {

    private val _onboardingInfoState = MutableStateFlow<MyPageOnboardingState>(MyPageOnboardingState.Idle)
    val onboardingInfoState: StateFlow<MyPageOnboardingState> = _onboardingInfoState.asStateFlow()

    // 온보딩 정보 조회
    fun getOnboardingInfo() = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        getOnboardingInfoUseCase.invoke()
            .onSuccess { onboardingInfo ->
                Timber.d("## 온보딩 정보 조회 성공 : $onboardingInfo")
                _onboardingInfoState.value = MyPageOnboardingState.Success(onboardingInfo)
            }.onFailure { e ->
                Timber.e("## 온보딩 정보 조회 실패 : ${e.message}")
                _onboardingInfoState.value = MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
            }
    }

}