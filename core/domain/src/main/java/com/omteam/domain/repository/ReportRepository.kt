package com.omteam.domain.repository

import com.omteam.domain.model.report.WeeklyReport
import kotlinx.coroutines.flow.Flow

interface ReportRepository {

    /**
     * 주간 리포트 조회
     * 
     * @param year 연도 (예: 2024), null인 경우 현재 주 기준
     * @param month 월 (1-12), null인 경우 현재 주 기준
     * @param weekOfMonth 해당 월의 주차 (1부터 시작), null인 경우 현재 주 기준
     * @return 주간 리포트 Flow
     */
    fun getWeeklyReport(
        year: Int? = null,
        month: Int? = null,
        weekOfMonth: Int? = null
    ): Flow<Result<WeeklyReport>>
}