package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.mission.DailyMissionRecommendation

/**
 * 데일리 미션 추천 받기 UI State
 */
sealed class DailyMissionRecommendationUiState {
    /**
     * 초기 상태
     */
    data object Idle : DailyMissionRecommendationUiState()
    
    /**
     * 로딩 중
     */
    data object Loading : DailyMissionRecommendationUiState()
    
    /**
     * 성공
     * @param data 추천된 미션 목록과 진행 중인 미션 정보
     */
    data class Success(val data: DailyMissionRecommendation) : DailyMissionRecommendationUiState()
    
    /**
     * 실패
     * @param message 에러 메시지
     */
    data class Error(val message: String) : DailyMissionRecommendationUiState()
}