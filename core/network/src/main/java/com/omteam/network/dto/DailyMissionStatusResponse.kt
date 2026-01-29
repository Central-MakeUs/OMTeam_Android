package com.omteam.network.dto

import kotlinx.serialization.Serializable

/**
 * 일일 미션 상태 조회 응답
 */
@Serializable
data class DailyMissionStatusResponse(
    val success: Boolean,
    val data: DailyMissionStatusData? = null,
    val error: ApiError? = null
)

@Serializable
data class DailyMissionStatusData(
    val date: String,
    val hasRecommendations: Boolean,
    val hasInProgressMission: Boolean,
    val hasCompletedMission: Boolean,
    val currentMission: CurrentMissionDto? = null,
    val missionResult: MissionResultDto? = null
)

@Serializable
data class CurrentMissionDto(
    val recommendedMissionId: Int,
    val missionDate: String,
    val status: String,
    val mission: MissionDto
)

@Serializable
data class MissionResultDto(
    val id: Int,
    val missionDate: String,
    val result: String,
    val failureReason: String? = null,
    val mission: MissionDto
)

@Serializable
data class MissionDto(
    val id: Int,
    val name: String,
    val type: String,
    val difficulty: Int,
    val estimatedMinutes: Int,
    val estimatedCalories: Int
)