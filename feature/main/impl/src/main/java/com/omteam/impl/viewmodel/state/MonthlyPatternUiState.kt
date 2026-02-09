package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.report.MonthlyPattern

/**
 * 월간 패턴 분석 UI 상태
 */
sealed class MonthlyPatternUiState {
    /**
     * 초기 상태 (아직 로드하지 않음)
     */
    data object Idle : MonthlyPatternUiState()

    /**
     * 로딩 중
     */
    data object Loading : MonthlyPatternUiState()

    /**
     * 로딩 성공
     */
    data class Success(val data: MonthlyPattern) : MonthlyPatternUiState()

    /**
     * 로딩 실패
     */
    data class Error(val message: String) : MonthlyPatternUiState()
}