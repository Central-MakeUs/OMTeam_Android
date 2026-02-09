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
     * @return 월간 패턴 분석 결과 Flow
     */
    operator fun invoke(): Flow<Result<MonthlyPattern>> =
        reportRepository.getMonthlyPattern()
}