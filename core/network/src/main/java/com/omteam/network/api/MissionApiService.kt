package com.omteam.network.api

import com.omteam.network.dto.mission.DailyMissionStatusResponse
import com.omteam.network.dto.mission.DailyRecommendedMissionsResponse
import com.omteam.network.dto.mission.DailyMissionRecommendResponse
import com.omteam.network.dto.mission.StartMissionRequest
import com.omteam.network.dto.mission.StartMissionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MissionApiService {
    
    /**
     * 일일 미션 상태 조회
     * 
     * @return 일일 미션 상태 정보 (추천 미션, 진행 중 미션, 완료된 미션 등)
     */
    @GET("api/missions/daily/status")
    suspend fun getDailyMissionStatus(): DailyMissionStatusResponse
    
    /**
     * 오늘의 추천 미션 목록 조회
     * 
     * @return 오늘의 추천 미션 목록
     */
    @GET("api/missions/daily/recommendations")
    suspend fun getDailyRecommendedMissions(): DailyRecommendedMissionsResponse
    
    /**
     * 데일리 미션 추천 받기
     * 
     * @return 추천된 미션 목록과 진행 중인 미션 정보
     */
    @POST("api/missions/daily/recommend")
    suspend fun requestDailyMissionRecommendations(): DailyMissionRecommendResponse
    
    /**
     * 미션 시작하기
     * 
     * @param request 시작할 미션 ID
     * @return 시작된 미션 정보
     */
    @POST("api/missions/daily/start")
    suspend fun startMission(@Body request: StartMissionRequest): StartMissionResponse
}