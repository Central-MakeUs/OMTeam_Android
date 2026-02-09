package com.omteam.domain.usecase

import com.omteam.domain.model.report.DailyFeedback
import com.omteam.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow

/**
 * 데일리 피드백 조회 UseCase
 *
 * @property reportRepository 리포트 Repository
 */
class GetDailyFeedbackUseCase(
    private val reportRepository: ReportRepository
) {
    /**
     * 데일리 피드백 조회
     *
     * @param date 조회할 날짜 (yyyy-MM-dd 형식), null인 경우 오늘 날짜 사용
     * @return 데일리 피드백 결과 Flow
     */
    operator fun invoke(date: String? = null): Flow<Result<DailyFeedback>> =
        reportRepository.getDailyFeedback(date)
}