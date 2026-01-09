package com.omteam.impl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.model.UserInfo
import com.omteam.domain.usecase.GetUserInfoUseCase
import com.omteam.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.credentials.ClearCredentialStateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val credentialManager: CredentialManager,
    @param:Named("google_web_client_id") private val googleWebClientId: String
): ViewModel() {

    private enum class LoginType {
        NONE, KAKAO, GOOGLE
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private var currentLoginType: LoginType = LoginType.NONE

    fun onLoginSuccess() {
        viewModelScope.launch {
            getUserInfoUseCase()
                .onSuccess { userInfo ->
                    Timber.d( "사용자 정보 조회 성공 : ${userInfo.email}")
                    _loginState.value = LoginState.Success(userInfo)
                }
                .onFailure { error ->
                    Timber.e( "사용자 정보 조회 실패 : ${error.message}")
                    _loginState.value = LoginState.Error(error.message ?: "사용자 정보 조회 실패")
                }
        }
    }

    fun onLoginStart() {
        _loginState.value = LoginState.Loading
    }

    fun onLoginFailure(message: String) {
        Timber.e( "onLoginFailure() - 에러 : $message")
        _loginState.value = LoginState.Error(message)
    }

    fun loginWithKakao() {
        currentLoginType = LoginType.KAKAO
        onLoginStart()
    }

    fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            currentLoginType = LoginType.GOOGLE

            GoogleLoginManager.signIn(
                context = context,
                credentialManager = credentialManager,
                webClientId = googleWebClientId
            ).onSuccess { googleCredential ->
                Timber.d( "GoogleLoginManager.signIn() 성공")
                // 구글 로그인 성공 시 GoogleIdTokenCredential에서 직접 정보 추출
                val userInfo = UserInfo(
                    id = googleCredential.id.hashCode().toLong(),
                    nickname = googleCredential.displayName,
                    email = googleCredential.id  // email
                )
                Timber.d( "UserInfo 생성 완료 : ${userInfo.email}")
                _loginState.value = LoginState.Success(userInfo)
            }.onFailure { error ->
                Timber.e( "GoogleLoginManager.signIn() 실패 : ${error.message}")
                currentLoginType = LoginType.NONE
                _loginState.value = LoginState.Error(error.message ?: "구글 로그인 실패")
            }
        }
    }

    fun logout() {
        Timber.d( "logout() 호출 - 현재 로그인 타입 : $currentLoginType")

        // 구글 로그아웃 속도가 느려서 UI 먼저 업데이트 후 백그라운드에서 로그아웃 처리
        val previousLoginType = currentLoginType
        currentLoginType = LoginType.NONE
        _loginState.value = LoginState.Idle

        viewModelScope.launch {
            when (previousLoginType) {
                LoginType.KAKAO -> {
                    Timber.d( "카카오 로그아웃 시작")
                    logoutUseCase()
                        .onSuccess {
                            Timber.d( "카카오 로그아웃 성공")
                        }
                        .onFailure { error ->
                            Timber.e( "카카오 로그아웃 실패 : ${error.message}")
                        }
                }
                LoginType.GOOGLE -> {
                    Timber.d( "구글 로그아웃 시작")
                    try {
                        credentialManager.clearCredentialState(
                            ClearCredentialStateRequest()
                        )
                        Timber.d( "구글 로그아웃 성공")
                    } catch (e: Exception) {
                        Timber.e( "구글 로그아웃 실패 : ${e.message}")
                    }
                }
                LoginType.NONE -> {}
            }
        }
    }

    sealed interface LoginState {
        data object Idle : LoginState
        data object Loading : LoginState
        data class Success(val userInfo: UserInfo) : LoginState
        data class Error(val message: String) : LoginState
    }

}