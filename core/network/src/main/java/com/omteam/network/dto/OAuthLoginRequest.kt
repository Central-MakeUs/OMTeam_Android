package com.omteam.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginRequest(
    val idToken: String
)