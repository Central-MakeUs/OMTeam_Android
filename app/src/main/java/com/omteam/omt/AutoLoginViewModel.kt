package com.omteam.omt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.data.fcm.FcmTokenSyncManager
import com.omteam.domain.usecase.AutoLoginResult
import com.omteam.domain.usecase.CheckAutoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AutoLoginViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
    private val fcmTokenSyncManager: FcmTokenSyncManager
) : ViewModel() {
    
    private val _autoLoginState = MutableStateFlow<AutoLoginState>(AutoLoginState.Checking)
    val autoLoginState: StateFlow<AutoLoginState> = _autoLoginState.asStateFlow()
    
    init {
        checkAutoLogin()
    }
    
    private fun checkAutoLogin() = viewModelScope.launch {
        Timber.d("## 자동 로그인 체크 시작")
        val result = checkAutoLoginUseCase()

        // 스플래시 화면 1.5초 동안 표시
        delay(1500)

        _autoLoginState.value = when (result) {
            is AutoLoginResult.NeedLogin -> {
                Timber.d("## [자동 로그인] 로그인 필요")
                AutoLoginState.NeedLogin
            }
            is AutoLoginResult.NeedOnboarding -> {
                Timber.d("## [자동 로그인] 온보딩 필요")
                AutoLoginState.NeedOnboarding
            }
            is AutoLoginResult.OnboardingCompleted -> {
                Timber.d("## [자동 로그인] 온보딩 완료 → 메인 화면")
                // FCM 토큰은 파베가 언제든 갱신할 수 있어서 자동 로그인 성공 시에도 최신 토큰을 서버에 등록
                registerFcmToken()
                AutoLoginState.NavigateToMain
            }
            is AutoLoginResult.TokenExpired -> {
                Timber.e("## [자동 로그인] 토큰 만료 → 로그인 화면")
                // TokenAuthenticator가 이미 토큰 삭제했으므로 중복 삭제 불필요
                AutoLoginState.NeedLogin
            }
        }
    }

    /**
     * FCM 토큰 등록
     *
     * 자동 로그인 성공(OnboardingCompleted) 시 백그라운드에서 호출
     *
     * Firebase가 토큰을 갱신했을 경우를 대비해 항상 최신 토큰을 서버에 등록
     */
    private fun registerFcmToken() = viewModelScope.launch {
        fcmTokenSyncManager.syncCurrentToken(source = "자동 로그인")
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