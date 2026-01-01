package com.omteam.domain.usecase

import com.omteam.domain.model.UserInfo
import com.omteam.domain.repository.AuthRepository

class GetUserInfoUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserInfo> = authRepository.getUserInfo()
}