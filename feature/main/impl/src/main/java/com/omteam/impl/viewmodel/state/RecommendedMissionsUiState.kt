package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.mission.RecommendedMission

/**
 * 추천 미션 목록 UI 상태
 */
sealed class RecommendedMissionsUiState {
    /**
     * 초기 상태 (아직 로드하지 않음)
     */
    data object Idle : RecommendedMissionsUiState()

    /**
     * 로딩 중
     */
    data object Loading : RecommendedMissionsUiState()

    /**
     * 로딩 성공
     */
    data class Success(val missions: List<RecommendedMission>) : RecommendedMissionsUiState()

    /**
     * 로딩 실패
     */
    data class Error(val message: String) : RecommendedMissionsUiState()
}
