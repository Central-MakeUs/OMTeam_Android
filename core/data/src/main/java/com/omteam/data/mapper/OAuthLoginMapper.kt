package com.omteam.data.mapper

import com.omteam.domain.model.auth.LoginResult
import com.omteam.network.dto.auth.LoginWithIdTokenData

fun LoginWithIdTokenData.toDomain(): LoginResult = LoginResult(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    onboardingCompleted = onboardingCompleted
)
