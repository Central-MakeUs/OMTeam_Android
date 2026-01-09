package com.omteam.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 로그인 화면 NavKey
 */
@Serializable
object LoginNavKey : NavKey

/**
 * 계정 연동 완료 화면 NavKey
 */
@Serializable
object AccountLinkCompleteNavKey : NavKey