package com.omteam.domain.usecase

import com.omteam.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: String): Result<Unit> = 
        authRepository.logout(provider)
}