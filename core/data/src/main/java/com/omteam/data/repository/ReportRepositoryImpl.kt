package com.omteam.data.repository

import com.omteam.data.mapper.toDomain
import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.domain.model.report.DailyFeedback
import com.omteam.domain.model.report.MonthlyPattern
import com.omteam.domain.model.report.WeeklyReport
import com.omteam.domain.repository.ReportRepository
import com.omteam.network.api.ReportApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportApiService: ReportApiService
) : ReportRepository {

    override fun getWeeklyReport(
        year: Int?,
        month: Int?,
        weekOfMonth: Int?
    ): Flow<Result<WeeklyReport>> =
        safeApiCall(
            logTag = "주간 리포트 조회",
            defaultErrorMessage = "주간 리포트를 불러올 수 없습니다",
            apiCall = { reportApiService.getWeeklyReport(year, month, weekOfMonth) },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun getDailyFeedback(date: String?): Flow<Result<DailyFeedback>> =
        safeApiCall(
            logTag = "데일리 피드백 조회",
            defaultErrorMessage = "데일리 피드백을 불러올 수 없습니다",
            apiCall = { reportApiService.getDailyFeedback(date) },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun getMonthlyPattern(): Flow<Result<MonthlyPattern>> =
        safeApiCall(
            logTag = "월간 패턴 분석 조회",
            defaultErrorMessage = "월간 패턴 분석을 불러올 수 없습니다",
            apiCall = { reportApiService.getMonthlyPattern() },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
}