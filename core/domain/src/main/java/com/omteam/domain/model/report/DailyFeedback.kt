package com.omteam.domain.model.report

import java.time.LocalDate

/**
 * 데일리 피드백 정보
 *
 * @property targetDate 피드백 대상 날짜
 * @property feedbackText 피드백 텍스트
 */
data class DailyFeedback(
    val targetDate: LocalDate,
    val feedbackText: String
)
