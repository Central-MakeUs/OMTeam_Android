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

/**
 * 운동 가능 시간 수정 화면 NavKey
 */
@Serializable
object EditExerciseTimeNavKey : NavKey

/**
 * 미션 투자 시간 수정 화면 NavKey
 */
@Serializable
object EditMissionTimeNavKey : NavKey

/**
 * 선호 운동 수정 화면 NavKey
 */
@Serializable
object EditFavoriteExerciseNavKey : NavKey

/**
 * 생활 패턴 수정 화면 NavKey
 */
@Serializable
object EditPatternNavKey : NavKey

/**
 * 웹뷰 화면 NavKey
 * 
 * @param url 표시할 웹페이지 URL
 */
@Serializable
data class WebViewNavKey(
    val url: String
) : NavKey