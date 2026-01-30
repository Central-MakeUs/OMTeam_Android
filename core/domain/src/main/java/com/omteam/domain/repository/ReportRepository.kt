package com.omteam.domain.repository

import com.omteam.domain.model.report.WeeklyReport
import kotlinx.coroutines.flow.Flow

/**
 * 리포트 Repository
 */
interface ReportRepository {

    /**
     * 주간 리포트 조회
     * 
     * @param weekStartDate 주차 시작 날짜 (형식: "2024-01-23")
     * @return 주간 리포트 Flow
     */
    fun getWeeklyReport(weekStartDate: String): Flow<Result<WeeklyReport>>
}