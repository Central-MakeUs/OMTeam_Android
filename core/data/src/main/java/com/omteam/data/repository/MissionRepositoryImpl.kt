package com.omteam.data.repository

import com.omteam.data.mapper.toDomain
import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.domain.model.DailyMissionStatus
import com.omteam.domain.repository.MissionRepository
import com.omteam.network.api.MissionApiService
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
}