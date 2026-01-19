package com.omteam.data.mapper

import com.omteam.domain.model.LoginResult
import com.omteam.network.dto.LoginWithIdTokenData

fun LoginWithIdTokenData.toDomain(): LoginResult = LoginResult(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    onboardingCompleted = onboardingCompleted
)
