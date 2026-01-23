package com.omteam.impl.model

/**
 * 온보딩 과정에서 수집하는 유저 정보
 * 
 * @property nickname 닉네임 (한글, 영어 포함 최대 8글자)
 * @property goal 목표
 * @property time 설정한 운동 가능 시간
 * @property missionTime 미션 수행에 투자 가능한 시간
 * @property favoriteExercise 선호하는 운동
 * @property pattern 최근 1달 간의 생활패턴과 가장 유사한 것
 * @property pushPermissionGranted 푸시 알림 허용 여부
 */
data class OnboardingData(
    val nickname: String = "",
    val goal: String = "",
    val time: String = "",
    val missionTime: String = "",
    val favoriteExercise: String = "",
    val pattern: String = "",
    val pushPermissionGranted: Boolean = false,
)
