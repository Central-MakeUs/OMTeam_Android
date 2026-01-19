package com.omteam.domain.usecase

import com.omteam.domain.model.LoginResult
import com.omteam.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * idToken으로 서버 로그인 UseCase
 *
 * 구글, 카카오에서 받은 idToken을 서버에 전송해서 accessToken 발급
 */
class LoginWithIdTokenUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * idToken으로 로그인
     *
     * @param provider google, kakao
     * @param idToken 구글, 카카오 로그인 성공 시 받는 idToken
     * @return 서버 로그인 결과 (accessToken, refreshToken 등) Flow
     */
    operator fun invoke(provider: String, idToken: String): Flow<Result<LoginResult>> =
        authRepository.loginWithIdToken(provider, idToken)
}