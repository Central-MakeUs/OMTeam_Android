package com.omteam.domain.usecase

import com.omteam.domain.model.report.MonthlyPattern
import com.omteam.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow

class GetMonthlyPatternUseCase(
    private val reportRepository: ReportRepository
) {
    /**
     * 월간 요일별 패턴 분석 조회
     *
     * @param year 연도 (예: 2024), null인 경우 현재 주 기준
     * @param month 월 (1-12), null인 경우 현재 주 기준
     * @param weekOfMonth 해당 월의 주차 (1부터 시작), null인 경우 현재 주 기준
     * @return 월간 패턴 분석 결과 Flow
     */
    operator fun invoke(
        year: Int? = null,
        month: Int? = null,
        weekOfMonth: Int? = null
    ): Flow<Result<MonthlyPattern>> =
        reportRepository.getMonthlyPattern(year, month, weekOfMonth)
}