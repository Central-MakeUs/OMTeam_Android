package com.omteam.impl.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.report.AiFeedback
import com.omteam.domain.model.report.DailyMissionStatus
import com.omteam.domain.model.report.DailyResult
import com.omteam.domain.model.report.DayOfWeek
import com.omteam.domain.model.report.DayOfWeekAiFeedback
import com.omteam.domain.model.report.DayOfWeekStat
import com.omteam.domain.model.report.MonthlyPattern
import com.omteam.domain.model.report.WeeklyReport
import com.omteam.impl.component.SubScreenHeader
import com.omteam.impl.viewmodel.ReportViewModel
import com.omteam.impl.viewmodel.state.MonthlyPatternUiState
import com.omteam.impl.viewmodel.state.WeeklyReportUiState
import com.omteam.omt.core.designsystem.R
import java.time.LocalDate
import java.util.Locale

/**
 * 상세 분석 보기 화면
 *
 * 주간 리포트의 상세 분석 정보를 표시
 *
 * @param onBackClick 뒤로가기 콜백
 */
@Composable
fun DetailedAnalysisScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: ReportViewModel = hiltViewModel()
) {
    val weeklyReportUiState = viewModel.weeklyReportUiState.collectAsStateWithLifecycle().value
    val monthlyPatternUiState = viewModel.monthlyPatternUiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.fetchWeeklyReport(useSelectedDate = false)
        viewModel.fetchMonthlyPattern()
    }

    DetailedAnalysisContent(
        modifier = modifier,
        onBackClick = onBackClick,
        weeklyReportUiState = weeklyReportUiState,
        monthlyPatternUiState = monthlyPatternUiState
    )
}

@Composable
private fun DetailedAnalysisContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    weeklyReportUiState: WeeklyReportUiState = WeeklyReportUiState.Idle,
    monthlyPatternUiState: MonthlyPatternUiState = MonthlyPatternUiState.Idle
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Gray02)
    ) {
        Spacer(modifier = Modifier.height(dp20))

        SubScreenHeader(
            title = "상세 분석 보기",
            onBackClick = onBackClick,
            modifier = Modifier
                .padding(horizontal = dp20)
                .background(Gray02)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = dp20)
        ) {
            Spacer(modifier = Modifier.height(dp12))

            when (weeklyReportUiState) {
                is WeeklyReportUiState.Success -> {
                    val weeklyReport = weeklyReportUiState.data

                    // 1번째 흰색 영역 - 성공률 비교
                    SuccessRateComparisonCard(weeklyReport = weeklyReport)

                    Spacer(modifier = Modifier.height(dp20))

                    // 2번째 흰색 영역 - 주간 미션 분석
                    WeeklyMissionAnalysisCard(weeklyReport = weeklyReport)

                    Spacer(modifier = Modifier.height(dp20))

                    // 3번째 흰색 영역 - 월간 요일별 패턴 분석
                    when (monthlyPatternUiState) {
                        is MonthlyPatternUiState.Success -> {
                            MonthlyPatternCard(monthlyPattern = monthlyPatternUiState.data)
                        }

                        else -> {
                            // 월간 패턴 로딩 중 또는 에러 - 플레이스홀더로 표시
                            ThirdCardPlaceholder()
                        }
                    }
                }

                else -> {
                    // 데이터 로딩 중 또는 에러 상태
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        OMTeamText(
                            text = "데이터를 불러오는 중...",
                            style = PretendardType.body02_2,
                            color = Gray08
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dp20))
        }
    }
}

/**
 * 1번째 흰색 영역 - 성공률 비교 카드
 */
@Composable
private fun SuccessRateComparisonCard(
    weeklyReport: WeeklyReport,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .background(White)
            .padding(dp12)
    ) {
        // 상단 아이콘과 제목
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_good),
                contentDescription = "성공률 상승",
                modifier = Modifier.size(dp24)
            )

            Spacer(modifier = Modifier.width(dp6))

            val comparisonText = when {
                weeklyReport.thisWeekSuccessRate > weeklyReport.lastWeekSuccessRate ->
                    "지난주보다 미션 성공률이 올랐어요!"

                weeklyReport.thisWeekSuccessRate < weeklyReport.lastWeekSuccessRate ->
                    "지난주보다 미션 성공률이 떨어졌어요"

                else ->
                    "지난주와 미션 성공률이 같아요"
            }

            OMTeamText(
                text = comparisonText,
                style = PaperlogyType.headline03,
                color = Black
            )
        }

        Spacer(modifier = Modifier.height(dp24))

        // 지난주 - 세로 표시
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OMTeamText(
                text = "지난주",
                style = PretendardType.button03Disabled,
                color = Gray09,
                modifier = Modifier.width(dp48)
            )

            Spacer(modifier = Modifier.width(dp16))

            Box(
                modifier = Modifier
                    .width(dp75)
                    .height(dp40)
                    .clip(RoundedCornerShape(dp8))
                    .background(Gray03),
                contentAlignment = Alignment.Center
            ) {
                OMTeamText(
                    text = "${
                        String.format(
                            Locale.getDefault(),
                            "%.1f",
                            weeklyReport.lastWeekSuccessRate
                        )
                    } %",
                    style = PretendardType.button03Abled,
                    color = Gray09
                )
            }
        }

        Spacer(modifier = Modifier.height(dp12))

        // 이번주 - 세로 표시
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OMTeamText(
                text = "이번주",
                style = PretendardType.button03Abled,
                color = Gray11,
                modifier = Modifier.width(dp48)
            )

            Spacer(modifier = Modifier.width(dp16))

            Box(
                modifier = Modifier
                    .width(dp112)
                    .height(dp40)
                    .clip(RoundedCornerShape(dp8))
                    .background(Green06),
                contentAlignment = Alignment.Center
            ) {
                OMTeamText(
                    text = "${
                        String.format(
                            Locale.getDefault(),
                            "%.1f",
                            weeklyReport.thisWeekSuccessRate
                        )
                    } %",
                    style = PretendardType.button03Abled,
                    color = Green12
                )
            }
        }
    }
}

/**
 * 2번째 흰색 영역 - 주간 미션 분석 카드
 */
@Composable
private fun WeeklyMissionAnalysisCard(
    weeklyReport: WeeklyReport,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .background(White)
            .padding(dp12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 아이콘과 제목
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_exercise),
                contentDescription = "주간 미션",
                modifier = Modifier.size(dp24)
            )

            Spacer(modifier = Modifier.width(dp6))

            OMTeamText(
                text = "이번주 미션, 한 번 살펴볼까요?",
                style = PaperlogyType.headline03,
                color = Black
            )
        }

        Spacer(modifier = Modifier.height(dp24))

        // 요일별 아이콘 표시
        WeeklyDayIconsRow(dailyResults = weeklyReport.dailyResults)

        Spacer(modifier = Modifier.height(dp20))

        // 미션 통계 텍스트 - typeSuccessCounts 사용
        val missionStatsText = weeklyReport.typeSuccessCounts.joinToString(
            separator = ", ",
            transform = { "${it.typeName} 미션 ${it.successCount}번" }
        )
        val totalSuccessCount = weeklyReport.thisWeekSuccessCount

        OMTeamText(
            text = "일주일 동안 ${missionStatsText}을 선택했어요. 총 ${totalSuccessCount}번 성공했어요!",
            style = PretendardType.body02_2,
            color = Gray11,
            modifier = Modifier.size(
                width = dp295,
                height = dp44
            )
        )

        Spacer(modifier = Modifier.height(dp20))

        // AI 피드백 영역
        AiFeedbackBox(aiFeedback = weeklyReport.aiFeedback)
    }
}

/**
 * 주간 요일별 아이콘 행
 */
@Composable
private fun WeeklyDayIconsRow(
    dailyResults: List<DailyResult>,
    modifier: Modifier = Modifier
) {
    val dayNames = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dp6)
    ) {
        dayNames.forEachIndexed { index, dayName ->
            val dailyResult = dailyResults.getOrNull(index)
            DayIconItem(
                dayName = dayName,
                dailyResult = dailyResult,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 요일별 아이콘 아이템
 */
@Composable
private fun DayIconItem(
    dayName: String,
    dailyResult: DailyResult?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconRes = when {
            dailyResult == null -> R.drawable.icon_exercise_failed
            dailyResult.missionType == "EXERCISE" && dailyResult.status == DailyMissionStatus.SUCCESS ->
                R.drawable.icon_exercise_success

            dailyResult.missionType == "EXERCISE" && dailyResult.status == DailyMissionStatus.FAILED ->
                R.drawable.icon_exercise_failed

            dailyResult.missionType == "DIET" && dailyResult.status == DailyMissionStatus.SUCCESS ->
                R.drawable.icon_diet_success

            dailyResult.missionType == "DIET" && dailyResult.status == DailyMissionStatus.FAILED ->
                R.drawable.icon_diet_failed

            else -> R.drawable.icon_exercise_failed
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "$dayName 미션 상태",
            modifier = Modifier.size(dp40)
        )

        Spacer(modifier = Modifier.height(dp4))

        OMTeamText(
            text = dayName,
            style = PretendardType.body04_2,
            color = Gray08
        )
    }
}

/**
 * AI 피드백 박스
 */
@Composable
private fun AiFeedbackBox(
    aiFeedback: AiFeedback,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp10))
            .border(
                width = dp1,
                color = Green04,
                shape = RoundedCornerShape(dp10)
            )
            .background(Green02)
            .padding(
                start = dp8,
                top = dp8,
                end = dp8,
                bottom = dp10
            )
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_exercise_failed),
                contentDescription = "AI 피드백",
                modifier = Modifier.size(dp40)
            )

            Spacer(modifier = Modifier.width(dp8))

            OMTeamText(
                text = aiFeedback.weeklyFeedback ?: "이번 주 미션 분석 데이터를 준비 중이에요!",
                style = PretendardType.body02_4,
                color = Gray11,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 3번째 흰색 영역 - 월간 요일별 패턴 분석 카드
 */
@Composable
private fun MonthlyPatternCard(
    monthlyPattern: MonthlyPattern,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .background(White)
            .padding(dp12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.status_success_apple),
                contentDescription = "월간 패턴",
                modifier = Modifier.size(dp24)
            )

            Spacer(modifier = Modifier.width(dp6))

            // AI 피드백 제목이 있으면 사용, 없으면 기본 제목
            val titleText = monthlyPattern.aiFeedback.dayOfWeekFeedbackTitle
                ?: "피드백 준비 중이에요"

            OMTeamText(
                text = titleText,
                style = PaperlogyType.headline03,
                color = Black
            )
        }

        Spacer(modifier = Modifier.height(dp24))

        // 요일별 그래프 표시
        WeeklyGraphRow(dayOfWeekStats = monthlyPattern.dayOfWeekStats)

        Spacer(modifier = Modifier.height(dp20))

        // AI 피드백 영역
        DayOfWeekFeedbackBox(aiFeedback = monthlyPattern.aiFeedback)
    }
}

/**
 * 요일별 그래프 Row
 */
@Composable
private fun WeeklyGraphRow(
    dayOfWeekStats: List<DayOfWeekStat>,
    modifier: Modifier = Modifier
) {
    // 요일 순서대로 정렬(일~토)
    val orderedDays =
        listOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")
    val orderedStats = orderedDays.map { day ->
        dayOfWeekStats.find { it.dayOfWeek == day }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dp4)
    ) {
        orderedStats.forEach { stat ->
            DayGraphItem(
                dayName = stat?.dayName ?: "",
                successCount = stat?.successCount ?: 0,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 요일별 그래프 아이템
 */
@Composable
private fun DayGraphItem(
    dayName: String,
    successCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val graphRes = when {
            successCount <= 0 -> R.drawable.graph_green_0
            successCount >= 5 -> R.drawable.graph_green_5
            else -> R.drawable.graph_gray_0
        }

        Image(
            painter = painterResource(id = graphRes),
            contentDescription = null,
            modifier = Modifier
                .width(dp36)
                .height(dp24)
        )

        Spacer(modifier = Modifier.height(dp8))

        OMTeamText(
            text = dayName,
            style = PretendardType.body04_2,
            color = Gray09
        )
    }
}

/**
 * 요일별 AI 피드백 박스
 */
@Composable
private fun DayOfWeekFeedbackBox(
    aiFeedback: DayOfWeekAiFeedback,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp10))
            .border(
                width = dp1,
                color = Green04,
                shape = RoundedCornerShape(dp10)
            )
            .background(Green02)
            .padding(
                start = dp8,
                top = dp8,
                end = dp8,
                bottom = dp10
            )
    ) {
        OMTeamText(
            text = aiFeedback.dayOfWeekFeedbackContent ?: "이번 달 요일별 미션 패턴 분석 데이터를 준비 중이에요!",
            style = PretendardType.body02_4,
            color = Gray11
        )
    }
}

/**
 * 3번째 흰색 영역 - 플레이스홀더
 */
@Composable
private fun ThirdCardPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .background(White)
            .padding(dp12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OMTeamText(
            text = "월간 패턴 분석 데이터를 불러오는 중...",
            style = PretendardType.body02_2,
            color = Gray08
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailedAnalysisScreenPreview() {
    val sampleWeeklyReport = WeeklyReport(
        weekStartDate = LocalDate.now().minusDays(6),
        weekEndDate = LocalDate.now(),
        thisWeekSuccessRate = 71.4,
        lastWeekSuccessRate = 28.9,
        thisWeekSuccessCount = 5,
        dailyResults = listOf(
            DailyResult(
                LocalDate.now().minusDays(6),
                DayOfWeek.SUNDAY,
                DailyMissionStatus.SUCCESS,
                "EXERCISE",
                "운동 미션"
            ),
            DailyResult(
                LocalDate.now().minusDays(5),
                DayOfWeek.MONDAY,
                DailyMissionStatus.SUCCESS,
                "EXERCISE",
                "운동 미션"
            ),
            DailyResult(
                LocalDate.now().minusDays(4),
                DayOfWeek.TUESDAY,
                DailyMissionStatus.FAILED,
                "NUTRITION",
                "식단 미션"
            ),
            DailyResult(
                LocalDate.now().minusDays(3),
                DayOfWeek.WEDNESDAY,
                DailyMissionStatus.SUCCESS,
                "EXERCISE",
                "운동 미션"
            ),
            DailyResult(
                LocalDate.now().minusDays(2),
                DayOfWeek.THURSDAY,
                DailyMissionStatus.NOT_PERFORMED,
                null,
                null
            ),
            DailyResult(
                LocalDate.now().minusDays(1),
                DayOfWeek.FRIDAY,
                DailyMissionStatus.SUCCESS,
                "NUTRITION",
                "식단 미션"
            ),
            DailyResult(
                LocalDate.now(),
                DayOfWeek.SATURDAY,
                DailyMissionStatus.SUCCESS,
                "EXERCISE",
                "운동 미션"
            )
        ),
        typeSuccessCounts = listOf(),
        topFailureReasons = listOf(),
        aiFeedback = AiFeedback(
            failureReasonRanking = emptyList(),
            weeklyFeedback = "운동 미션을 꾸준히 수행하셨네요! 다음 주도 화이팅!"
        )
    )

    val sampleMonthlyPattern = MonthlyPattern(
        startDate = LocalDate.now().minusDays(30),
        endDate = LocalDate.now(),
        dayOfWeekStats = listOf(
            DayOfWeekStat("SUNDAY", "일요일", 2, 2, 0, 100.0),
            DayOfWeekStat("MONDAY", "월요일", 3, 2, 1, 67.5),
            DayOfWeekStat("TUESDAY", "화요일", 3, 3, 0, 100.0),
            DayOfWeekStat("WEDNESDAY", "수요일", 1, 0, 1, 0.0),
            DayOfWeekStat("THURSDAY", "목요일", 2, 2, 0, 100.0),
            DayOfWeekStat("FRIDAY", "금요일", 4, 3, 1, 75.2),
            DayOfWeekStat("SATURDAY", "토요일", 3, 3, 0, 100.0)
        ),
        aiFeedback = DayOfWeekAiFeedback(
            dayOfWeekFeedbackTitle = "수요일엔 쉬어가는 건 어때요?",
            dayOfWeekFeedbackContent = "수요일은 성공률이 낮은 날이에요. 수요일에는 좀 더 쉬운 미션을 선택해보는 건 어떨까요?"
        )
    )

    OMTeamTheme {
        DetailedAnalysisContent(
            weeklyReportUiState = WeeklyReportUiState.Success(sampleWeeklyReport),
            monthlyPatternUiState = MonthlyPatternUiState.Success(sampleMonthlyPattern)
        )
    }
}
