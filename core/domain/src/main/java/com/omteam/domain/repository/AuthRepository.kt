package com.omteam.domain.repository

import com.omteam.domain.model.UserInfo

interface AuthRepository {
    suspend fun getUserInfo(): Result<UserInfo>
    suspend fun logout(): Result<Unit>
}