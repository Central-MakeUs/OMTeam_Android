package com.omteam.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 메인 화면 NavKey
 */
@Serializable
object MainNavKey : NavKey

/**
 * 기타 화면 NavKey
 */
@Serializable
object OtherNavKey : NavKey

/**
 * 나의 목표 수정하기 화면 NavKey
 */
@Serializable
object EditMyGoalNavKey : NavKey

/**
 * 내 정보 수정하기 화면 NavKey
 */
@Serializable
object EditMyInfoNavKey : NavKey