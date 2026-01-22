package com.omteam.impl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.datastore.TokenDataStore
import com.omteam.domain.model.LoginResult
import com.omteam.domain.model.UserInfo
import com.omteam.domain.usecase.GetUserInfoUseCase
import com.omteam.domain.usecase.LogoutUseCase
import com.omteam.domain.usecase.LoginWithIdTokenUseCase
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
    private val loginWithIdTokenUseCase: LoginWithIdTokenUseCase,
    private val credentialManager: CredentialManager,
    private val tokenDataStore: TokenDataStore,
    @param:Named("google_web_client_id") private val googleWebClientId: String
): ViewModel() {

    private enum class LoginType {
        NONE, KAKAO, GOOGLE
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private var currentLoginType: LoginType = LoginType.NONE

    fun loginWithKakao(context: Context) = viewModelScope.launch {
        _loginState.value = LoginState.Loading
        currentLoginType = LoginType.KAKAO

        try {
            val result = if (KakaoLoginManager.isKakaoTalkAvailable(context)) {
                Timber.d("## 카카오톡으로 로그인 시도")
                KakaoLoginManager.loginWithKakaoTalk(context)
            } else {
                Timber.d("## 카카오 계정으로 로그인 시도")
                KakaoLoginManager.loginWithKakaoAccount(context)
            }

            result.onSuccess { oAuthToken ->
                Timber.d("## 카카오 로그인 성공 - idToken: ${oAuthToken.idToken}")

                // idToken이 없을 때
                val idToken = oAuthToken.idToken
                if (idToken == null) {
                    Timber.e("## 카카오 ID Token이 null입니다")
                    currentLoginType = LoginType.NONE
                    _loginState.value = LoginState.Error("카카오 ID Token을 받을 수 없습니다")
                    return@launch
                }

                // 서버에 idToken 전송하여 로그인
                loginWithIdTokenUseCase("kakao", idToken).collect { apiResult ->
                    apiResult.onSuccess { loginResult ->
                        Timber.d("## 서버 로그인 성공 - 온보딩 완료: ${loginResult.onboardingCompleted}")

                        // 토큰 저장
                        tokenDataStore.saveTokens(loginResult.accessToken, loginResult.refreshToken)
                        Timber.d("## 카카오 로그인 후 토큰 저장 완료")

                        // 사용자 정보 조회
                        getUserInfoUseCase("kakao")
                            .onSuccess { userInfo ->
                                Timber.d("## 카카오 유저 정보 조회 성공: ${userInfo.email}")
                                _loginState.value = LoginState.Success(loginResult, userInfo)
                            }
                            .onFailure { error ->
                                Timber.e("## 카카오 유저 정보 조회 실패: ${error.message}")
                                currentLoginType = LoginType.NONE
                                _loginState.value = LoginState.Error(error.message ?: "유저 정보 조회 실패")
                            }
                    }.onFailure { error ->
                        Timber.e("## 서버 로그인 실패 : ${error.message}")
                        currentLoginType = LoginType.NONE
                        _loginState.value = LoginState.Error(error.message ?: "서버 로그인 실패")
                    }
                }
            }.onFailure { error ->
                Timber.e("## 카카오 로그인 실패 : ${error.message}")
                currentLoginType = LoginType.NONE
                _loginState.value = LoginState.Error(error.message ?: "카카오 로그인 실패")
            }
        } catch (e: Exception) {
            Timber.e("## 카카오 로그인 에러 : ${e.message}")
            currentLoginType = LoginType.NONE
            _loginState.value = LoginState.Error(e.message ?: "카카오 로그인 에러")
        }
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
                Timber.d("## 구글 로그인 성공 - idToken: ${googleCredential.idToken}")

                val userInfo = UserInfo(
                    id = googleCredential.id.hashCode().toLong(),
                    nickname = googleCredential.displayName,
                    email = googleCredential.id
                )

                // 서버에 idToken 보내 로그인
                loginWithIdTokenUseCase("google", googleCredential.idToken).collect { apiResult ->
                    apiResult.onSuccess { loginResult ->
                        Timber.d("## 서버 로그인 성공 - 온보딩 완료: ${loginResult.onboardingCompleted}")

                        tokenDataStore.saveTokens(loginResult.accessToken, loginResult.refreshToken)
                        Timber.d("## 구글 로그인 후 토큰 저장 완료")
                        
                        // 로그인 시 받은 유저 정보 확인
                        Timber.d("## 구글 유저 정보 : ${userInfo.email}")
                        _loginState.value = LoginState.Success(loginResult, userInfo)
                    }.onFailure { error ->
                        Timber.e("## 서버 로그인 실패 : ${error.message}")
                        currentLoginType = LoginType.NONE
                        _loginState.value = LoginState.Error(error.message ?: "서버 로그인 실패")
                    }
                }
            }.onFailure { error ->
                Timber.e("## 구글 로그인 실패 : ${error.message}")
                currentLoginType = LoginType.NONE
                _loginState.value = LoginState.Error(error.message ?: "구글 로그인 실패")
            }
        }
    }

    fun logout() {
        Timber.d( "## logout() 호출 - 현재 로그인 타입 : $currentLoginType")

        // 구글 로그아웃 속도가 느려서 UI 먼저 업데이트 후 백그라운드에서 로그아웃 처리
        val previousLoginType = currentLoginType
        currentLoginType = LoginType.NONE
        _loginState.value = LoginState.Idle

        viewModelScope.launch {
            // 저장된 토큰 삭제
            tokenDataStore.clearTokens()
            Timber.d("## 토큰 삭제 완료")

            when (previousLoginType) {
                LoginType.KAKAO -> {
                    Timber.d("## 카카오 로그아웃 시작")
                    logoutUseCase("kakao")
                        .onSuccess {
                            Timber.d("## 카카오 로그아웃 성공")
                        }
                        .onFailure { error ->
                            Timber.e("## 카카오 로그아웃 실패 : ${error.message}")
                        }
                }
                LoginType.GOOGLE -> {
                    Timber.d("## 구글 로그아웃 시작")
                    logoutUseCase("google")
                        .onSuccess {
                            Timber.d("## 구글 로그아웃 성공")
                        }
                        .onFailure { error ->
                            Timber.e("## 구글 로그아웃 실패 : ${error.message}")
                        }
                }
                LoginType.NONE -> {}
            }
        }
    }

    // 로그인 상태 완전 초기화
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    sealed interface LoginState {
        data object Idle : LoginState
        data object Loading : LoginState
        data class Success(
            val loginResult: LoginResult,
            val userInfo: UserInfo
        ) : LoginState
        data class Error(val message: String) : LoginState
    }

}