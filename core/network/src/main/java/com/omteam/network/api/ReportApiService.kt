package com.omteam.network.api

import com.omteam.network.dto.report.WeeklyReportResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApiService {

    /**
     * 주간 리포트 조회
     * 
     * @param weekStartDate 주차 시작 날짜 (형식: "2024-01-23")
     */
    @GET("api/reports/weekly")
    suspend fun getWeeklyReport(
        @Query("weekStartDate") weekStartDate: String
    ): WeeklyReportResponse
}