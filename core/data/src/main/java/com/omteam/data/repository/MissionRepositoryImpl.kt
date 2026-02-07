package com.omteam.data.repository

import com.omteam.data.mapper.toDomain
import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.domain.model.mission.CurrentMission
import com.omteam.domain.model.mission.DailyMissionRecommendation
import com.omteam.domain.model.mission.DailyMissionStatus
import com.omteam.domain.model.mission.RecommendedMission
import com.omteam.domain.repository.MissionRepository
import com.omteam.network.api.MissionApiService
import com.omteam.network.dto.mission.StartMissionRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MissionRepositoryImpl @Inject constructor(
    private val missionApiService: MissionApiService
) : MissionRepository {

    override fun getDailyMissionStatus(): Flow<Result<DailyMissionStatus>> =
        safeApiCall(
            logTag = "일일 미션 상태 조회",
            defaultErrorMessage = "일일 미션 상태를 불러올 수 없습니다",
            apiCall = { missionApiService.getDailyMissionStatus() },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
    
    override fun getDailyRecommendedMissions(): Flow<Result<List<RecommendedMission>>> =
        safeApiCall(
            logTag = "오늘의 추천 미션 목록 조회",
            defaultErrorMessage = "오늘의 추천 미션 목록을 불러올 수 없습니다",
            apiCall = { missionApiService.getDailyRecommendedMissions() },
            transform = { response -> response.data?.map { it.toDomain() } },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
    
    override fun requestDailyMissionRecommendations(): Flow<Result<DailyMissionRecommendation>> =
        safeApiCall(
            logTag = "데일리 미션 추천 받기",
            defaultErrorMessage = "데일리 미션 추천을 받을 수 없습니다",
            apiCall = { missionApiService.requestDailyMissionRecommendations() },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
    
    override fun startMission(recommendedMissionId: Int): Flow<Result<CurrentMission>> =
        safeApiCall(
            logTag = "미션 시작하기",
            defaultErrorMessage = "미션을 시작할 수 없습니다",
            apiCall = { missionApiService.startMission(StartMissionRequest(recommendedMissionId)) },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
}