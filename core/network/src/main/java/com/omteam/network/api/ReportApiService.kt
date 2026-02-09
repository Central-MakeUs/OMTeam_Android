package com.omteam.network.api

import com.omteam.network.dto.report.DailyFeedbackResponse
import com.omteam.network.dto.report.MonthlyPatternResponse
import com.omteam.network.dto.report.WeeklyReportResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApiService {

    /**
     * 주간 리포트 조회
     *
     * @param year 연도 (예: 2024)
     * @param month 월 (1-12)
     * @param weekOfMonth 해당 월의 주차 (1부터 시작)
     */
    @GET("api/reports/weekly")
    suspend fun getWeeklyReport(
        @Query("year") year: Int? = null,
        @Query("month") month: Int? = null,
        @Query("weekOfMonth") weekOfMonth: Int? = null
    ): WeeklyReportResponse

    /**
     * 데일리 피드백 조회
     *
     * @param date 조회할 날짜 (yyyy-MM-dd 형식), null인 경우 오늘 날짜 사용
     */
    @GET("api/reports/daily/feedback")
    suspend fun getDailyFeedback(
        @Query("date") date: String? = null
    ): DailyFeedbackResponse

    /**
     * 월간 요일별 패턴 분석 조회
     */
    @GET("api/reports/monthly-pattern")
    suspend fun getMonthlyPattern(): MonthlyPatternResponse
}