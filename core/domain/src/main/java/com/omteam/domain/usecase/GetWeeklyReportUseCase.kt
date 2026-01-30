package com.omteam.domain.usecase

import com.omteam.domain.model.report.WeeklyReport
import com.omteam.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow

/**
 * 주간 리포트 조회 UseCase
 */
class GetWeeklyReportUseCase(
    private val reportRepository: ReportRepository
) {
    /**
     * 주간 리포트 조회
     * 
     * @param weekStartDate 주차 시작 날짜 (형식: "2024-01-23"), null인 경우 현재 주 기준
     */
    operator fun invoke(weekStartDate: String? = null): Flow<Result<WeeklyReport>> =
        reportRepository.getWeeklyReport(weekStartDate)
}