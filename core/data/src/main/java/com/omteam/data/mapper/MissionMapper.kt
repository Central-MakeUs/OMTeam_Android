package com.omteam.data.mapper

import com.omteam.domain.model.CurrentMission
import com.omteam.domain.model.DailyMissionStatus
import com.omteam.domain.model.Mission
import com.omteam.domain.model.MissionResult
import com.omteam.domain.model.MissionResultType
import com.omteam.domain.model.MissionStatus
import com.omteam.domain.model.MissionType
import com.omteam.network.dto.CurrentMissionDto
import com.omteam.network.dto.DailyMissionStatusData
import com.omteam.network.dto.MissionDto
import com.omteam.network.dto.MissionResultDto
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