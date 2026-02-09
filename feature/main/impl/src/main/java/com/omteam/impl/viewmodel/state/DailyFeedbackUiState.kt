package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.report.DailyFeedback

/**
 * 데일리 피드백 UI 상태
 */
sealed class DailyFeedbackUiState {
    /**
     * 초기 상태 (아직 로드하지 않음)
     */
    data object Idle : DailyFeedbackUiState()

    /**
     * 로딩 중
     */
    data object Loading : DailyFeedbackUiState()

    /**
     * 로딩 성공
     */
    data class Success(val data: DailyFeedback) : DailyFeedbackUiState()

    /**
     * 로딩 실패
     */
    data class Error(val message: String) : DailyFeedbackUiState()
}