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
 * 
 * @param initialExerciseTime 초기 운동 시간대 값
 */
@Serializable
data class EditExerciseTimeNavKey(
    val initialExerciseTime: String = ""
) : NavKey

/**
 * 미션 투자 시간 수정 화면 NavKey
 * 
 * @param initialAvailableTime 초기 미션 투자 시간 값
 */
@Serializable
data class EditMissionTimeNavKey(
    val initialAvailableTime: String = ""
) : NavKey

/**
 * 선호 운동 수정 화면 NavKey
 * 
 * @param initialFavoriteExercises 초기 선호 운동 리스트
 */
@Serializable
data class EditFavoriteExerciseNavKey(
    val initialFavoriteExercises: List<String> = emptyList()
) : NavKey

/**
 * 생활 패턴 수정 화면 NavKey
 * 
 * @param initialPattern 초기 생활 패턴 값
 */
@Serializable
data class EditPatternNavKey(
    val initialPattern: String = ""
) : NavKey

/**
 * 웹뷰 화면 NavKey
 * 
 * @param url 표시할 웹페이지 URL
 */
@Serializable
data class WebViewNavKey(
    val url: String
) : NavKey