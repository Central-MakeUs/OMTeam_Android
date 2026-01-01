package com.omteam.data.repository

import com.omteam.data.datasource.AuthDataSource
import com.omteam.domain.model.UserInfo
import com.omteam.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * AuthDataSource 통해 구글, 카카오 로그인 지원
 */
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun getUserInfo(): Result<UserInfo> = authDataSource.getUserInfo()

    override suspend fun logout(): Result<Unit> = authDataSource.logout()
}