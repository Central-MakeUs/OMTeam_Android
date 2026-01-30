package com.omteam.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginWithIdTokenRequest(
    val idToken: String
)