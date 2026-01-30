package com.omteam.impl.viewmodel

import com.omteam.domain.model.report.WeeklyReport

/**
 * 주간 리포트 UI 상태
 */
sealed class WeeklyReportUiState {
    /**
     * 초기 상태 (아직 로드하지 않음)
     */
    data object Idle : WeeklyReportUiState()
    
    /**
     * 로딩 중
     */
    data object Loading : WeeklyReportUiState()
    
    /**
     * 로딩 성공
     */
    data class Success(val data: WeeklyReport) : WeeklyReportUiState()
    
    /**
     * 로딩 실패
     */
    data class Error(val message: String) : WeeklyReportUiState()
}
