package com.omteam.domain.model.report

import java.time.LocalDate

/**
 * 월간 요일별 패턴 분석 정보
 *
 * @property startDate 분석 시작 날짜
 * @property endDate 분석 종료 날짜
 * @property dayOfWeekStats 요일별 통계 목록
 * @property aiFeedback AI 피드백 정보
 */
data class MonthlyPattern(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dayOfWeekStats: List<DayOfWeekStat>,
    val aiFeedback: DayOfWeekAiFeedback
)

/**
 * 요일별 통계 정보
 *
 * @property dayOfWeek 요일 (MONDAY, TUESDAY 등)
 * @property dayName 요일 이름 (월요일, 화요일 등)
 * @property totalCount 총 미션 수
 * @property successCount 성공 횟수
 * @property failureCount 실패 횟수
 * @property successRate 성공률 (%)
 */
data class DayOfWeekStat(
    val dayOfWeek: String,
    val dayName: String,
    val totalCount: Int,
    val successCount: Int,
    val failureCount: Int,
    val successRate: Double
)

/**
 * 요일별 AI 피드백 정보
 *
 * @property dayOfWeekFeedbackTitle 피드백 제목
 * @property dayOfWeekFeedbackContent 피드백 내용
 */
data class DayOfWeekAiFeedback(
    val dayOfWeekFeedbackTitle: String? = null,
    val dayOfWeekFeedbackContent: String? = null
)