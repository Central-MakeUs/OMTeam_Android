package com.omteam.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 온보딩 화면 NavKey
 *
 * @param step 현재 온보딩 단계 (1~7)
 */
@Serializable
data class OnboardingNavKey(
    val step: Int = 1
) : NavKey {
    companion object {
        const val TOTAL_STEPS = 7
    }
}