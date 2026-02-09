package com.omteam.domain.repository

import com.omteam.domain.model.mission.CurrentMission
import com.omteam.domain.model.mission.DailyMissionRecommendation
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
    
    /**
     * 데일리 미션 추천 받기
     * 
     * @return 추천된 미션 목록과 진행 중인 미션 정보 Flow
     */
    fun requestDailyMissionRecommendations(): Flow<Result<DailyMissionRecommendation>>
    
    /**
     * 미션 시작하기
     * 
     * @param recommendedMissionId 추천 미션 ID
     * @return 시작된 미션 정보 Flow
     */
    fun startMission(recommendedMissionId: Int): Flow<Result<CurrentMission>>
}