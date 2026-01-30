package com.omteam.network.api

import com.omteam.network.dto.mission.DailyMissionStatusResponse
import retrofit2.http.GET

interface MissionApiService {
    
    /**
     * 일일 미션 상태 조회
     * 
     * @return 일일 미션 상태 정보 (추천 미션, 진행 중 미션, 완료된 미션 등)
     */
    @GET("api/missions/daily/status")
    suspend fun getDailyMissionStatus(): DailyMissionStatusResponse
}