package com.omteam.domain.usecase

import com.omteam.domain.model.mission.CurrentMission
import com.omteam.domain.repository.MissionRepository
import kotlinx.coroutines.flow.Flow

/**
 * 미션 시작하기 UseCase
 * 
 * 사용자가 선택한 추천 미션을 시작합니다.
 */
class StartMissionUseCase(
    private val missionRepository: MissionRepository
) {
    /**
     * 미션 시작하기
     * 
     * @param recommendedMissionId 추천 미션 ID
     * @return 시작된 미션 정보
     */
    operator fun invoke(recommendedMissionId: Int): Flow<Result<CurrentMission>> =
        missionRepository.startMission(recommendedMissionId)
}