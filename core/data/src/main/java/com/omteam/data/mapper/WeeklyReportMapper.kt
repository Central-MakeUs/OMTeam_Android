package com.omteam.data.mapper

import com.omteam.domain.model.report.AiFeedback
import com.omteam.domain.model.report.DailyFeedback
import com.omteam.domain.model.report.DailyMissionStatus
import com.omteam.domain.model.report.DailyResult
import com.omteam.domain.model.report.DayOfWeek
import com.omteam.domain.model.report.DayOfWeekAiFeedback
import com.omteam.domain.model.report.DayOfWeekStat
import com.omteam.domain.model.report.FailureReasonRanking
import com.omteam.domain.model.report.MonthlyPattern
import com.omteam.domain.model.report.TopFailureReason
import com.omteam.domain.model.report.TypeSuccessCount
import com.omteam.domain.model.report.WeeklyReport
import com.omteam.network.dto.report.AiFeedbackDto
import com.omteam.network.dto.report.DailyFeedbackData
import com.omteam.network.dto.report.DailyResultDto
import com.omteam.network.dto.report.DayOfWeekAiFeedbackDto
import com.omteam.network.dto.report.DayOfWeekStatDto
import com.omteam.network.dto.report.FailureReasonRankingDto
import com.omteam.network.dto.report.MonthlyPatternData
import com.omteam.network.dto.report.TopFailureReasonDto
import com.omteam.network.dto.report.TypeSuccessCountDto
import com.omteam.network.dto.report.WeeklyReportData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * WeeklyReportData -> WeeklyReport 도메인 모델
 */
fun WeeklyReportData.toDomain(): WeeklyReport = WeeklyReport(
    weekStartDate = weekStartDate.toLocalDate(),
    weekEndDate = weekEndDate.toLocalDate(),
    thisWeekSuccessRate = thisWeekSuccessRate,
    lastWeekSuccessRate = lastWeekSuccessRate,
    thisWeekSuccessCount = thisWeekSuccessCount,
    dailyResults = dailyResults.map { it.toDomain() },
    typeSuccessCounts = typeSuccessCounts.map { it.toDomain() },
    topFailureReasons = topFailureReasons.map { it.toDomain() },
    aiFeedback = aiFeedback.toDomain()
)

/**
 * DailyResultDto -> DailyResult 도메인 모델
 */
fun DailyResultDto.toDomain(): DailyResult = DailyResult(
    date = date.toLocalDate(),
    dayOfWeek = DayOfWeek.fromString(dayOfWeek),
    status = DailyMissionStatus.fromString(status),
    missionType = missionType,
    missionTitle = missionTitle
)

/**
 * TypeSuccessCountDto -> TypeSuccessCount 도메인 모델
 */
fun TypeSuccessCountDto.toDomain(): TypeSuccessCount = TypeSuccessCount(
    type = type,
    typeName = typeName,
    successCount = successCount
)

/**
 * TopFailureReasonDto -> TopFailureReason 도메인 모델
 */
fun TopFailureReasonDto.toDomain(): TopFailureReason = TopFailureReason(
    rank = rank,
    reason = reason,
    count = count
)

/**
 * AiFeedbackDto -> AiFeedback 도메인 모델
 */
fun AiFeedbackDto.toDomain(): AiFeedback = AiFeedback(
    failureReasonRanking = failureReasonRanking?.map { it.toDomain() },
    weeklyFeedback = weeklyFeedback
)

/**
 * FailureReasonRankingDto -> FailureReasonRanking 도메인 모델
 */
fun FailureReasonRankingDto.toDomain(): FailureReasonRanking = FailureReasonRanking(
    rank = rank,
    category = category,
    count = count
)

/**
 * 날짜 문자열 -> LocalDate
 */
private fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)

/**
 * DailyFeedbackData -> DailyFeedback 도메인 모델
 */
fun DailyFeedbackData.toDomain(): DailyFeedback = DailyFeedback(
    targetDate = targetDate.toLocalDate(),
    feedbackText = feedbackText
)

/**
 * MonthlyPatternData -> MonthlyPattern 도메인 모델
 */
fun MonthlyPatternData.toDomain(): MonthlyPattern = MonthlyPattern(
    startDate = startDate.toLocalDate(),
    endDate = endDate.toLocalDate(),
    dayOfWeekStats = dayOfWeekStats.map { it.toDomain() },
    aiFeedback = aiFeedback.toDomain()
)

/**
 * DayOfWeekStatDto -> DayOfWeekStat 도메인 모델
 */
fun DayOfWeekStatDto.toDomain(): DayOfWeekStat = DayOfWeekStat(
    dayOfWeek = dayOfWeek,
    dayName = dayName,
    totalCount = totalCount,
    successCount = successCount,
    failureCount = failureCount,
    successRate = successRate
)

/**
 * DayOfWeekAiFeedbackDto -> DayOfWeekAiFeedback 도메인 모델
 */
fun DayOfWeekAiFeedbackDto.toDomain(): DayOfWeekAiFeedback = DayOfWeekAiFeedback(
    dayOfWeekFeedbackTitle = dayOfWeekFeedbackTitle,
    dayOfWeekFeedbackContent = dayOfWeekFeedbackContent
)