package com.omteam.domain.usecase

import com.omteam.domain.model.OnboardingInfo
import com.omteam.domain.repository.AuthRepository

class CheckAutoLoginUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * 자동 로그인 체크
     * 
     * @return AutoLoginResult (토큰 유무, 온보딩 완료 여부)
     */
    suspend operator fun invoke(): AutoLoginResult {
        val hasToken = authRepository.hasAccessToken()
        
        if (!hasToken) {
            return AutoLoginResult.NeedLogin
        }
        
        // 온보딩 정보 조회
        return authRepository.getOnboardingInfo()
            .fold(
                onSuccess = { onboardingInfo ->
                    // 온보딩 다 끝냈으면 메인 화면 이동
                    AutoLoginResult.OnboardingCompleted(onboardingInfo)
                },
                onFailure = { error ->
                    val errorMessage = error.message ?: ""
                    
                    // 온보딩 미완이면 온보딩 1번 화면으로 이동
                    if (errorMessage.contains("U003") || errorMessage.contains("온보딩")) {
                        AutoLoginResult.NeedOnboarding
                    }
                    // 토큰 만료 or 인증 에러 (401, 403 등) → 토큰 삭제
                    else if (errorMessage.contains("403") || errorMessage.contains("401") || 
                             errorMessage.contains("Unauthorized") || errorMessage.contains("Forbidden")) {
                        AutoLoginResult.TokenExpired
                    }
                    // 기타 네트워크 에러
                    else {
                        AutoLoginResult.NetworkError(errorMessage)
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
    
    // 토큰 만료 or 인증 에러 (로그인 화면 이동하며 토큰 삭제)
    data object TokenExpired : AutoLoginResult
    
    // 네트워크 에러 (재시도해야 함)
    data class NetworkError(val message: String) : AutoLoginResult
}
