package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 평소 생활 패턴 수정 요청
 */
@Serializable
data class UpdateLifestyleRequest(
    val lifestyleType: String
)