package com.omteam.data.mapper

import com.omteam.domain.model.mission.CurrentMission
import com.omteam.domain.model.mission.DailyMissionRecommendation
import com.omteam.domain.model.mission.DailyMissionStatus
import com.omteam.domain.model.mission.Mission
import com.omteam.domain.model.mission.MissionResult
import com.omteam.domain.model.mission.MissionResultType
import com.omteam.domain.model.mission.MissionStatus
import com.omteam.domain.model.mission.MissionType
import com.omteam.domain.model.mission.RecommendedMission
import com.omteam.network.dto.mission.CurrentMissionDto
import com.omteam.network.dto.mission.DailyMissionRecommendData
import com.omteam.network.dto.mission.DailyMissionStatusData
import com.omteam.network.dto.mission.MissionDto
import com.omteam.network.dto.mission.MissionResultDto
import com.omteam.network.dto.mission.RecommendedMissionDto
import java.time.LocalDate

/**
 * DailyMissionStatusData -> DailyMissionStatus 도메인 모델
 */
fun DailyMissionStatusData.toDomain(): DailyMissionStatus = DailyMissionStatus(
    date = LocalDate.parse(date),
    hasRecommendations = hasRecommendations,
    hasInProgressMission = hasInProgressMission,
    hasCompletedMission = hasCompletedMission,
    currentMission = currentMission?.toDomain(),
    missionResult = missionResult?.toDomain()
)

/**
 * CurrentMissionDto -> CurrentMission 도메인 모델
 */
fun CurrentMissionDto.toDomain(): CurrentMission = CurrentMission(
    recommendedMissionId = recommendedMissionId,
    missionDate = LocalDate.parse(missionDate),
    status = MissionStatus.fromString(status),
    mission = mission.toDomain()
)

/**
 * MissionResultDto -> MissionResult 도메인 모델
 */
fun MissionResultDto.toDomain(): MissionResult = MissionResult(
    id = id,
    missionDate = LocalDate.parse(missionDate),
    result = MissionResultType.fromString(result),
    failureReason = failureReason,
    mission = mission.toDomain()
)

/**
 * MissionDto -> Mission 도메인 모델
 */
fun MissionDto.toDomain(): Mission = Mission(
    id = id,
    name = name,
    type = MissionType.fromString(type),
    difficulty = difficulty,
    estimatedMinutes = estimatedMinutes,
    estimatedCalories = estimatedCalories
)

/**
 * RecommendedMissionDto -> RecommendedMission 도메인 모델
 */
fun RecommendedMissionDto.toDomain(): RecommendedMission = RecommendedMission(
    recommendedMissionId = recommendedMissionId,
    missionDate = LocalDate.parse(missionDate),
    status = MissionStatus.fromString(status),
    mission = mission.toDomain()
)

/**
 * DailyMissionRecommendData -> DailyMissionRecommendation 도메인 모델
 */
fun DailyMissionRecommendData.toDomain(): DailyMissionRecommendation = DailyMissionRecommendation(
    missionDate = LocalDate.parse(missionDate),
    recommendations = recommendations.map { it.toDomain() },
    hasInProgressMission = hasInProgressMission,
    inProgressMission = inProgressMission?.toDomain()
)