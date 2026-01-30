package com.omteam.domain.model.report

import java.time.LocalDate

/**
 * 주간 리포트 정보
 * 
 * @property weekStartDate 주차 시작 날짜
 * @property weekEndDate 주차 종료 날짜
 * @property thisWeekSuccessRate 이번 주 성공률
 * @property lastWeekSuccessRate 지난 주 성공률
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
 * @property missionType 미션 타입
 * @property missionTitle 미션 제목
 */
data class DailyResult(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val status: DailyMissionStatus,
    val missionType: String,
    val missionTitle: String
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
 * @property mainFailureReason 주요 실패 사유
 * @property overallFeedback 전체 피드백
 */
data class AiFeedback(
    val mainFailureReason: String,
    val overallFeedback: String
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
    NONE,
    UNKNOWN;

    companion object {
        fun fromString(value: String): DailyMissionStatus =
            DailyMissionStatus.entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}