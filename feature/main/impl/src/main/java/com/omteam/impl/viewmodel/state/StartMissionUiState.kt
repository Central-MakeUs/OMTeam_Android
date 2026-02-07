package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.mission.CurrentMission

/**
 * 미션 시작하기 UI State
 */
sealed class StartMissionUiState {
    /**
     * 초기 상태
     */
    data object Idle : StartMissionUiState()
    
    /**
     * 로딩 중
     */
    data object Loading : StartMissionUiState()
    
    /**
     * 성공
     * @param data 시작된 미션 정보
     */
    data class Success(val data: CurrentMission) : StartMissionUiState()
    
    /**
     * 실패
     * @param message 에러 메시지
     */
    data class Error(val message: String) : StartMissionUiState()
}