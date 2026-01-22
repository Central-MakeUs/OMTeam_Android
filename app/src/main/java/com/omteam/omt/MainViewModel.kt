package com.omteam.omt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.repository.AuthRepository
import com.omteam.domain.usecase.AutoLoginResult
import com.omteam.domain.usecase.CheckAutoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _autoLoginState = MutableStateFlow<AutoLoginState>(AutoLoginState.Checking)
    val autoLoginState: StateFlow<AutoLoginState> = _autoLoginState.asStateFlow()
    
    init {
        checkAutoLogin()
    }
    
    private fun checkAutoLogin() = viewModelScope.launch {
        Timber.d("## 자동 로그인 체크 시작")
        val result = checkAutoLoginUseCase()

        _autoLoginState.value = when (result) {
            is AutoLoginResult.NeedLogin -> {
                Timber.d("## 자동 로그인 : 로그인 필요")
                AutoLoginState.NeedLogin
            }
            is AutoLoginResult.NeedOnboarding -> {
                Timber.d("## 자동 로그인 : 온보딩 필요")
                AutoLoginState.NeedOnboarding
            }
            is AutoLoginResult.OnboardingCompleted -> {
                Timber.d("## 자동 로그인 : 온보딩 완료 → 메인 화면")
                AutoLoginState.NavigateToMain
            }
            is AutoLoginResult.TokenExpired -> {
                Timber.e("## 자동 로그인 : 토큰 만료 → 토큰 삭제 후 로그인 화면")
                // 만료된 토큰 삭제
                authRepository.clearTokens()
                AutoLoginState.NeedLogin
            }
            is AutoLoginResult.NetworkError -> {
                Timber.e("## 자동 로그인 : 네트워크 에러 - ${result.message}")
                // 네트워크 오류는 토큰을 유지하되 로그인 화면으로
                AutoLoginState.NeedLogin
            }
        }
    }
    
    sealed interface AutoLoginState {
        // 체크 중
        data object Checking : AutoLoginState
        
        // 로그인 화면 이동
        data object NeedLogin : AutoLoginState

        // 온보딩 화면 이동
        data object NeedOnboarding : AutoLoginState

        // 메인 화면 이동
        data object NavigateToMain : AutoLoginState
    }
}
