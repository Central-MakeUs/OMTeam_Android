package com.omteam.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginWithIdTokenRequest(
    val idToken: String
)