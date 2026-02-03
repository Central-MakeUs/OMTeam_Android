package com.omteam.domain.usecase

import com.omteam.domain.model.mission.RecommendedMission
import com.omteam.domain.repository.MissionRepository
import kotlinx.coroutines.flow.Flow

class GetDailyRecommendedMissionsUseCase(
    private val missionRepository: MissionRepository
) {
    operator fun invoke(): Flow<Result<List<RecommendedMission>>> =
        missionRepository.getDailyRecommendedMissions()
}