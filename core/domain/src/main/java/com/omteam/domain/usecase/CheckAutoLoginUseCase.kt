package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.AuthRepository
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.first

class CheckAutoLoginUseCase(
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository
) {
    /**
     * 자동 로그인 체크
     * 
     * @return AutoLoginResult (토큰 유무, 온보딩 완료 여부)
     */
    suspend operator fun invoke(): AutoLoginResult {
        val hasAccessToken = authRepository.hasAccessToken()
        if (!hasAccessToken) {
            return AutoLoginResult.NeedLogin
        }
        
        // 온보딩 정보 조회
        // 401, 403은 TokenAuthenticator가 자동 처리
        // - 갱신 성공 시 재시도 -> 200 OK 받음
        // - 갱신 실패 시 TokenAuthenticator가 토큰 삭제 후 에러 발생
        return onboardingRepository.getOnboardingInfo()
            .first()
            .fold(
                onSuccess = { onboardingInfo ->
                    // 온보딩 완료 → 메인 화면
                    AutoLoginResult.OnboardingCompleted(onboardingInfo)
                },
                onFailure = { error ->
                    val errorMessage = error.message ?: ""
                    
                    // 온보딩 미완료 (U003)
                    if (errorMessage.contains("U003") || errorMessage.contains("온보딩")) {
                        AutoLoginResult.NeedOnboarding
                    } else {
                        // TokenAuthenticator가 토큰 갱신 실패 시(네트워크, 서버 에러 포함) 토큰 삭제하고 예외 발생
                        // 로그인 화면으로 이동
                        AutoLoginResult.TokenExpired
                    }
                }
            )
    }
}

/**
 * 자동 로그인 결과
 */
sealed interface AutoLoginResult {
    // 로그인 필요 (토큰 없음)
    data object NeedLogin : AutoLoginResult
    
    // 온보딩 필요 (토큰이 저장돼 있지만 온보딩 미완)
    data object NeedOnboarding : AutoLoginResult
    
    // 온보딩 완료 (메인 화면 이동)
    data class OnboardingCompleted(val onboardingInfo: OnboardingInfo) : AutoLoginResult
    
    // 토큰 만료 or 에러 (로그인 화면으로 이동, TokenAuthenticator가 토큰 삭제 완료)
    data object TokenExpired : AutoLoginResult
}