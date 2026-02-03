package com.omteam.domain.model.mission

import java.time.LocalDate

/**
 * 일일 미션 상태 정보
 * 
 * @property date 조회한 날짜
 * @property hasRecommendations 추천 미션이 있는지 여부
 * @property hasInProgressMission 진행 중인 미션이 있는지 여부
 * @property hasCompletedMission 완료된 미션이 있는지 여부
 * @property currentMission 현재 미션 정보
 * @property missionResult 미션 결과 정보
 */
data class DailyMissionStatus(
    val date: LocalDate,
    val hasRecommendations: Boolean,
    val hasInProgressMission: Boolean,
    val hasCompletedMission: Boolean,
    val currentMission: CurrentMission? = null,
    val missionResult: MissionResult? = null
)

/**
 * 현재 미션 정보
 * 
 * @property recommendedMissionId 추천 미션 id
 * @property missionDate 미션 날짜
 * @property status 미션 상태 (RECOMMENDED)
 * @property mission 미션 상세 정보
 */
data class CurrentMission(
    val recommendedMissionId: Int,
    val missionDate: LocalDate,
    val status: MissionStatus,
    val mission: Mission
)

/**
 * 미션 결과 정보
 * 
 * @property id
 * @property missionDate 미션 날짜
 * @property result 미션 결과 (성공 / 실패)
 * @property failureReason 실패 사유 (실패한 경우)
 * @property mission 미션 상세 정보
 */
data class MissionResult(
    val id: Int,
    val missionDate: LocalDate,
    val result: MissionResultType,
    val failureReason: String? = null,
    val mission: Mission
)

/**
 * 미션 상세 정보
 * 
 * @property id
 * @property name 미션 이름
 * @property type 미션 타입 (EXERCISE)
 * @property difficulty 난이도
 * @property estimatedMinutes 예상 소요 시간 (분)
 * @property estimatedCalories 예상 소모 칼로리
 */
data class Mission(
    val id: Int,
    val name: String,
    val type: MissionType,
    val difficulty: Int,
    val estimatedMinutes: Int,
    val estimatedCalories: Int
)

/**
 * 미션 상태
 */
enum class MissionStatus {
    RECOMMENDED,
    IN_PROGRESS,
    COMPLETED,
    UNKNOWN;
    
    companion object {
        fun fromString(value: String): MissionStatus =
            MissionStatus.entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}

/**
 * 미션 타입
 */
enum class MissionType {
    EXERCISE,
    NUTRITION,
    LIFESTYLE,
    UNKNOWN;
    
    companion object {
        fun fromString(value: String): MissionType =
            MissionType.entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}

/**
 * 미션 결과 타입
 */
enum class MissionResultType {
    SUCCESS,
    FAILED,
    UNKNOWN;
    
    companion object {
        fun fromString(value: String): MissionResultType =
            MissionResultType.entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}

/**
 * 추천 미션 정보
 * 
 * @property recommendedMissionId 추천 미션 id
 * @property missionDate 미션 날짜
 * @property status 미션 상태
 * @property mission 미션 상세 정보
 */
data class RecommendedMission(
    val recommendedMissionId: Int,
    val missionDate: LocalDate,
    val status: MissionStatus,
    val mission: Mission
)