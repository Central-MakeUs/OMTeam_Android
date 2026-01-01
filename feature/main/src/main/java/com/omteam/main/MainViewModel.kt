package com.omteam.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.model.UserInfo
import com.omteam.domain.usecase.GetUserInfoUseCase
import com.omteam.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun onLoginSuccess() {
        viewModelScope.launch {
            getUserInfoUseCase()
                .onSuccess { userInfo ->
                    _loginState.value = LoginState.Success(userInfo)
                }
                .onFailure { error ->
                    _loginState.value = LoginState.Error(error.message ?: "사용자 정보 조회 실패")
                }
        }
    }

    fun onLoginStart() {
        _loginState.value = LoginState.Loading
    }

    fun onLoginFailure(message: String) {
        _loginState.value = LoginState.Error(message)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
                .onSuccess {
                    _loginState.value = LoginState.Idle
                }
                .onFailure { error ->
                    _loginState.value = LoginState.Error(error.message ?: "로그아웃 실패")
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