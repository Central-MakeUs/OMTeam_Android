package com.omteam.domain.model.report

import java.time.LocalDate

/**
 * 주간 리포트 정보
 * 
 * @property weekStartDate 주차 시작 날짜
 * @property weekEndDate 주차 종료 날짜
 * @property thisWeekSuccessRate 이번 주 성공률
 * @property lastWeekSuccessRate 지난 주 성공률
 * @property thisWeekSuccessCount 이번 주 성공 횟수
 * @property dailyResults 일일 결과 목록
 * @property typeSuccessCounts 미션 타입별 성공 횟수 목록
 * @property topFailureReasons 상위 실패 사유 목록
 * @property aiFeedback AI 피드백
 */
data class WeeklyReport(
    val weekStartDate: LocalDate,
    val weekEndDate: LocalDate,
    val thisWeekSuccessRate: Double,
    val lastWeekSuccessRate: Double,
    val thisWeekSuccessCount: Int,
    val dailyResults: List<DailyResult>,
    val typeSuccessCounts: List<TypeSuccessCount>,
    val topFailureReasons: List<TopFailureReason>,
    val aiFeedback: AiFeedback
)

/**
 * 일일 결과
 * 
 * @property date 날짜
 * @property dayOfWeek 요일
 * @property status 미션 상태
 * @property missionType 미션 타입 (미션이 없는 경우 null)
 * @property missionTitle 미션 제목 (미션이 없는 경우 null)
 */
data class DailyResult(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val status: DailyMissionStatus,
    val missionType: String? = null,
    val missionTitle: String? = null
)

/**
 * 미션 타입별 성공 횟수
 * 
 * @property type 미션 타입 (EXERCISE, NUTRITION 등)
 * @property typeName 미션 타입 이름 (운동, 영양 등)
 * @property successCount 성공 횟수
 */
data class TypeSuccessCount(
    val type: String,
    val typeName: String,
    val successCount: Int
)

/**
 * 상위 실패 사유
 * 
 * @property rank 순위
 * @property reason 실패 사유
 * @property count 횟수
 */
data class TopFailureReason(
    val rank: Int,
    val reason: String,
    val count: Int
)

/**
 * AI 피드백
 * 
 * @property failureReasonRanking 실패 사유 순위 목록
 * @property weeklyFeedback 주간 피드백
 */
data class AiFeedback(
    val failureReasonRanking: List<FailureReasonRanking>,
    val weeklyFeedback: String
)

/**
 * 실패 사유 순위
 * 
 * @property rank 순위
 * @property category 실패 사유 카테고리
 * @property count 횟수
 */
data class FailureReasonRanking(
    val rank: Int,
    val category: String,
    val count: Int
)

/**
 * 요일
 */
enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    UNKNOWN;

    companion object {
        fun fromString(value: String): DayOfWeek =
            DayOfWeek.entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}

/**
 * 일일 미션 상태
 */
enum class DailyMissionStatus {
    SUCCESS,
    FAILED,
    NOT_PERFORMED,
    NONE,
    UNKNOWN;

    companion object {
        fun fromString(value: String): DailyMissionStatus =
            DailyMissionStatus.entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}