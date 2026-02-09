package com.omteam.network.dto.report

import kotlinx.serialization.Serializable

/**
 * 데일리 피드백 응답 DTO
 *
 * @property success API 요청 성공 여부
 * @property data 데일리 피드백 데이터
 * @property error 에러 정보
 */
@Serializable
data class DailyFeedbackResponse(
    val success: Boolean,
    val data: DailyFeedbackData? = null,
    val error: DailyFeedbackError? = null
)

/**
 * 데일리 피드백 데이터
 *
 * @property targetDate 피드백 대상 날짜
 * @property feedbackText 피드백 텍스트
 */
@Serializable
data class DailyFeedbackData(
    val targetDate: String,
    val feedbackText: String
)

/**
 * 데일리 피드백 에러 정보
 *
 * @property code 에러 코드
 * @property message 에러 메시지
 */
@Serializable
data class DailyFeedbackError(
    val code: String,
    val message: String
)
