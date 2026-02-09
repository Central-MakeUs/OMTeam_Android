package com.omteam.domain.usecase

import com.omteam.domain.repository.AuthRepository

class WithdrawUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * 회원탈퇴 실행
     *
     * @return 탈퇴 결과 메시지
     */
    suspend operator fun invoke(): Result<String> =
        authRepository.withdraw()
}