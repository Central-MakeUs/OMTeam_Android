package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 앱 사용 목적 수정 요청
 *
 * @param appGoalText 앱 사용 목적
 */
@Serializable
data class UpdateAppGoalRequest(
    val appGoalText: String
)