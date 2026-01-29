package com.omteam.domain.repository

import com.omteam.domain.model.DailyMissionStatus
import kotlinx.coroutines.flow.Flow

interface MissionRepository {
    /**
     * 일일 미션 상태 조회
     * 
     * @return 일일 미션 상태 정보 Flow
     */
    fun getDailyMissionStatus(): Flow<Result<DailyMissionStatus>>
}