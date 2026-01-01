package com.omteam.data.repository

import com.omteam.data.datasource.KakaoAuthDataSource
import com.omteam.data.mapper.UserInfoMapper
import com.omteam.domain.model.UserInfo
import com.omteam.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakaoAuthDataSource: KakaoAuthDataSource
) : AuthRepository {

    override suspend fun getUserInfo(): Result<UserInfo> = runCatching {
        val kakaoUser = kakaoAuthDataSource.getUserInfo().getOrThrow()
        UserInfoMapper.toDomain(kakaoUser)
    }

    override suspend fun logout(): Result<Unit> = kakaoAuthDataSource.logout()
}