package com.omteam.impl.viewmodel

import com.omteam.domain.model.mission.DailyMissionStatus

/**
 * 일일 미션 UI 상태
 */
sealed class DailyMissionUiState {
    /**
     * 초기 상태 (아직 로드하지 않음)
     */
    data object Idle : DailyMissionUiState()

    /**
     * 로딩 중
     */
    data object Loading : DailyMissionUiState()

    /**
     * 로딩 성공
     */
    data class Success(val data: DailyMissionStatus) : DailyMissionUiState()

    /**
     * 로딩 실패
     */
    data class Error(val message: String) : DailyMissionUiState()
}
