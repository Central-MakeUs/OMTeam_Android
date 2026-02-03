package com.omteam.domain.repository

import com.omteam.domain.model.mission.DailyMissionStatus
import com.omteam.domain.model.mission.RecommendedMission
import kotlinx.coroutines.flow.Flow

interface MissionRepository {
    /**
     * 일일 미션 상태 조회
     * 
     * @return 일일 미션 상태 정보 Flow
     */
    fun getDailyMissionStatus(): Flow<Result<DailyMissionStatus>>
    
    /**
     * 오늘의 추천 미션 목록 조회
     * 
     * @return 오늘의 추천 미션 목록 Flow
     */
    fun getDailyRecommendedMissions(): Flow<Result<List<RecommendedMission>>>
}