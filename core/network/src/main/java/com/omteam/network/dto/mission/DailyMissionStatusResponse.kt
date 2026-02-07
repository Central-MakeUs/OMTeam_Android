package com.omteam.network.dto.mission

import com.omteam.network.dto.onboarding.ApiError
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

/**
 * 오늘의 추천 미션 목록 조회 응답
 */
@Serializable
data class DailyRecommendedMissionsResponse(
    val success: Boolean,
    val data: List<RecommendedMissionDto>? = null,
    val error: ApiError? = null
)

@Serializable
data class RecommendedMissionDto(
    val recommendedMissionId: Int,
    val missionDate: String,
    val status: String,
    val mission: MissionDto
)

/**
 * 데일리 미션 추천 받기 응답
 */
@Serializable
data class DailyMissionRecommendResponse(
    val success: Boolean,
    val data: DailyMissionRecommendData? = null,
    val error: ApiError? = null
)

@Serializable
data class DailyMissionRecommendData(
    val missionDate: String,
    val recommendations: List<RecommendedMissionDto>,
    val hasInProgressMission: Boolean,
    val inProgressMission: CurrentMissionDto? = null
)

/**
 * 미션 시작 요청
 */
@Serializable
data class StartMissionRequest(
    val recommendedMissionId: Int
)

/**
 * 미션 시작 응답
 */
@Serializable
data class StartMissionResponse(
    val success: Boolean,
    val data: CurrentMissionDto? = null,
    val error: ApiError? = null
)