package com.omteam.domain.usecase

import com.omteam.domain.model.mission.DailyMissionRecommendation
import com.omteam.domain.repository.MissionRepository
import kotlinx.coroutines.flow.Flow

/**
 * 데일리 미션 추천 받기 UseCase
 * 
 * 사용자에게 오늘의 추천 미션 목록을 생성하고 반환합니다.
 */
class RequestDailyMissionRecommendationsUseCase(
    private val missionRepository: MissionRepository
) {
    /**
     * 데일리 미션 추천 받기
     * 
     * @return 추천된 미션 목록과 진행 중인 미션 정보
     */
    operator fun invoke(): Flow<Result<DailyMissionRecommendation>> =
        missionRepository.requestDailyMissionRecommendations()
}