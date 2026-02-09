package com.omteam.impl.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.report.AiFeedback
import com.omteam.domain.model.report.DailyFeedback
import com.omteam.domain.model.report.DailyMissionStatus
import com.omteam.domain.model.report.DailyResult
import com.omteam.domain.model.report.DayOfWeek
import com.omteam.domain.model.report.TopFailureReason
import com.omteam.domain.model.report.WeeklyReport
import com.omteam.impl.viewmodel.ReportViewModel
import com.omteam.impl.viewmodel.state.DailyFeedbackUiState
import com.omteam.impl.viewmodel.state.WeeklyReportUiState
import com.omteam.omt.core.designsystem.R
import timber.log.Timber
import java.time.LocalDate

@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    reportViewModel: ReportViewModel = hiltViewModel(),
    onNavigateToDetailedAnalysis: () -> Unit = {}
) {
    val weekDisplayText by reportViewModel.weekDisplayText.collectAsState()
    val weeklyReportUiState by reportViewModel.weeklyReportUiState.collectAsState()
    val dailyFeedbackUiState by reportViewModel.dailyFeedbackUiState.collectAsState()

    // 화면 진입 시 데이터 로드
    LaunchedEffect(Unit) {
        reportViewModel.fetchWeeklyReport()
        reportViewModel.fetchDailyFeedbackForSelectedDate()
    }

    ReportContent(
        modifier = modifier,
        weekDisplayText = weekDisplayText,
        weeklyReportUiState = weeklyReportUiState,
        dailyFeedbackUiState = dailyFeedbackUiState,
        onPreviousWeekClick = { reportViewModel.moveToPreviousWeek() },
        onNextWeekClick = { reportViewModel.moveToNextWeek() },
        onWeekSelectClick = { Timber.d("## 주 선택") },
        onRefreshClick = {
            reportViewModel.resetToCurrentWeek()
            reportViewModel.fetchWeeklyReport()
            reportViewModel.fetchDailyFeedbackForSelectedDate()
        },
        onDetailedAnalysisClick = onNavigateToDetailedAnalysis
    )
}

@Composable
fun ReportContent(
    modifier: Modifier = Modifier,
    weekDisplayText: String,
    weeklyReportUiState: WeeklyReportUiState = WeeklyReportUiState.Idle,
    dailyFeedbackUiState: DailyFeedbackUiState = DailyFeedbackUiState.Idle,
    onPreviousWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    onWeekSelectClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onDetailedAnalysisClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Gray02)
            .padding(dp20)
            .verticalScroll(rememberScrollState()),
    ) {
        Image(
            painter = painterResource(id = R.drawable.screen_inner_logo),
            contentDescription = "왼쪽 상단 로고",
            modifier = Modifier.size(dp50)
        )

        Spacer(modifier = Modifier.height(dp26))

        // 주 선택, 새로고침 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_arrow_back),
                contentDescription = "이전 주",
                modifier = Modifier.size(dp24)
                    .clickable { onPreviousWeekClick() }
            )

            Spacer(modifier = Modifier.height(dp12))

            // 기본값 : 이번 주
            OMTeamText(
                text = weekDisplayText,
                style = PretendardType.button02Enabled
            )

            Spacer(modifier = Modifier.height(dp4))

            Image(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "주 선택 아이콘",
                modifier = Modifier.size(dp24)
                    .clickable { onWeekSelectClick() }
            )

            Spacer(modifier = Modifier.height(dp12))

            Image(
                painter = painterResource(id = R.drawable.icon_arrow_forth),
                contentDescription = "다음 주",
                modifier = Modifier.size(dp24)
                    .clickable { onNextWeekClick() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.icon_update_able),
                contentDescription = "새로고침",
                modifier = Modifier.size(dp24)
                    .clickable { onRefreshClick() }
            )
        }

        Spacer(modifier = Modifier.height(dp20))

        // 리포트 데이터 표시 영역
        when (weeklyReportUiState) {
            is WeeklyReportUiState.Success -> {
                // 데이터가 있으면 리포트 화면 표시
                ReportDataContent(
                    weeklyReport = weeklyReportUiState.data,
                    dailyFeedbackUiState = dailyFeedbackUiState,
                    onDetailedAnalysisClick = onDetailedAnalysisClick
                )
            }
            else -> {
                // 데이터가 없거나 로딩/에러 상태면 초기 화면 표시
                EmptyReportContent(modifier = Modifier.fillMaxWidth())
            }
        }

        // 하단 여백
        Spacer(modifier = Modifier.height(dp28))
    }
}

/**
 * 데이터가 없을 때 표시되는 초기 화면
 */
@Composable
private fun EmptyReportContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dp102))

        Box(
            modifier = Modifier.size(dp175)
        ) {
            Image(
                painterResource(id = R.drawable.character_embarrassed_normal),
                contentDescription = "레포트 탭 데이터 없음",
                modifier = Modifier.size(dp175)
            )
        }

        Spacer(modifier = Modifier.height(dp54))

        OMTeamText(
            text = stringResource(com.omteam.main.impl.R.string.empty_report_data),
            style = PaperlogyType.headline01
        )

        Spacer(modifier = Modifier.height(dp16))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Green08)) {
                    append(stringResource(com.omteam.main.impl.R.string.empty_report_data_first))
                }
                append(stringResource(com.omteam.main.impl.R.string.empty_report_data_second))
            },
            style = PretendardType.body02,
            color = Gray09,
            fontSize = 15.sp
        )
    }
}

/**
 * 데이터가 있을 때 표시되는 리포트 화면
 */
@Composable
private fun ReportDataContent(
    weeklyReport: WeeklyReport,
    dailyFeedbackUiState: DailyFeedbackUiState,
    onDetailedAnalysisClick: () -> Unit
) {
    // 이번주 미션 성공률
    SuccessRateCard(
        thisWeekSuccessRate = weeklyReport.thisWeekSuccessRate,
        thisWeekSuccessCount = weeklyReport.thisWeekSuccessCount,
        dailyResults = weeklyReport.dailyResults
    )

    Spacer(modifier = Modifier.height(dp20))

    // 미션이 힘들었던 이유
    FailureReasonCard(topFailureReasons = weeklyReport.topFailureReasons)

    Spacer(modifier = Modifier.height(dp20))

    // OMT의 제안 (데일리 피드백)
    SuggestionCard(dailyFeedbackUiState = dailyFeedbackUiState)

    Spacer(modifier = Modifier.height(dp28))

    OMTeamButton(
        text = "더 자세한 분석 보기",
        onClick = onDetailedAnalysisClick,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * 성공률 카드 (1번째 흰색 영역)
 */
@Composable
private fun SuccessRateCard(
    thisWeekSuccessRate: Double,
    thisWeekSuccessCount: Int,
    dailyResults: List<DailyResult>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .background(White)
            .padding(vertical = dp4)
            .padding(bottom = dp8),
        horizontalAlignment = Alignment.Start
    ) {
        // 상단 아이콘과 텍스트 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp16, vertical = dp10),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_mission),
                contentDescription = null,
                modifier = Modifier.size(dp40)
            )

            Spacer(modifier = Modifier.width(dp16))

            Column {
                OMTeamText(
                    text = stringResource(com.omteam.main.impl.R.string.this_week_success_rate),
                    style = PretendardType.body02_3,
                    color = Gray10
                )

                Spacer(modifier = Modifier.height(dp4))

                // 성공률 퍼센트와 성공 횟수를 같은 줄에 표시
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    OMTeamText(
                        text = "${thisWeekSuccessRate.toInt()}%",
                        style = PaperlogyType.headline01,
                        color = Black
                    )

                    Spacer(modifier = Modifier.width(dp8))

                    // 성공 횟수 표시 (07은 1주일 기준)
                    OMTeamText(
                        text = "$thisWeekSuccessCount/07",
                        style = PretendardType.body04_4,
                        color = Gray07
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dp16))

        // 요일별 사과 표시
        DaysAppleRow(dailyResults = dailyResults)
    }
}

/**
 * 요일별 사과 행
 */
@Composable
private fun DaysAppleRow(dailyResults: List<DailyResult>) {
    val dayNames = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dp16),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 7일치 데이터 표시
        // 데이터가 7개 미만이면 빈 값으로 채움
        dayNames.forEachIndexed { index, dayName ->
            val dailyResult = dailyResults.getOrNull(index)
            DayAppleItem(
                dayName = dayName,
                status = dailyResult?.status ?: DailyMissionStatus.NONE
            )
        }
    }
}

/**
 * 요일별 사과 아이템
 */
@Composable
private fun DayAppleItem(
    dayName: String,
    status: DailyMissionStatus
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OMTeamText(
            text = dayName,
            style = PretendardType.body04_2,
            color = Gray08
        )

        Spacer(modifier = Modifier.height(dp8))

        Image(
            painter = painterResource(
                id = when (status) {
                    DailyMissionStatus.SUCCESS -> R.drawable.status_success_apple
                    DailyMissionStatus.FAILED -> R.drawable.status_failed_apple
                    else -> R.drawable.status_default_apple
                }
            ),
            contentDescription = "사과 상태",
            modifier = Modifier.size(dp24)
        )
    }
}

/**
 * 미션이 힘들었던 이유를 살펴봤어요!
 */
@Composable
private fun FailureReasonCard(topFailureReasons: List<TopFailureReason>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .background(White)
            .padding(horizontal = dp12, vertical = dp10)
            .padding(bottom = dp14)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_good),
                contentDescription = null,
                modifier = Modifier.size(dp24)
            )

            Spacer(modifier = Modifier.width(dp8))

            OMTeamText(
                text = stringResource(com.omteam.main.impl.R.string.failure_reasons),
                style = PaperlogyType.headline04,
                color = Gray11
            )
        }

        Spacer(modifier = Modifier.height(dp20))

        // 실패 사유 목록 (모든 항목 표시)
        topFailureReasons.forEachIndexed { index, reason ->
            if (index > 0) {
                Spacer(modifier = Modifier.height(dp12))
            }

            FailureReasonItem(
                rank = reason.rank,
                reason = reason.reason,
                isFirst = index == 0
            )
        }
    }
}

/**
 * 실패 사유 아이템
 */
@Composable
private fun FailureReasonItem(
    rank: Int,
    reason: String,
    isFirst: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 순위 텍스트
        OMTeamText(
            text = "${rank}위",
            style = PretendardType.button03Abled,
            color = if (isFirst) Green08 else GreenSub07Button
        )

        Spacer(modifier = Modifier.height(dp16))

        // 사유 텍스트
        OMTeamText(
            text = reason,
            style = if (isFirst) PretendardType.button01Enabled else PretendardType.button02Disabled,
            color = if (isFirst) Black else Gray10
        )
    }
}

/**
 * 제안 카드 (3번째 흰색 영역)
 */
@Composable
private fun SuggestionCard(dailyFeedbackUiState: DailyFeedbackUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp12))
            .background(White)
            .padding(horizontal = dp12, vertical = dp12)
            .padding(bottom = dp14),
        verticalArrangement = Arrangement.spacedBy(dp14)
    ) {
        // 상단 아이콘과 제목
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_suggestion),
                contentDescription = null,
                modifier = Modifier.size(dp24)
            )

            Spacer(modifier = Modifier.width(dp8))

            OMTeamText(
                text = stringResource(com.omteam.main.impl.R.string.suggestion),
                style = PaperlogyType.headline03,
                color = Gray11
            )
        }

        // 데일리 피드백 텍스트
        when (dailyFeedbackUiState) {
            is DailyFeedbackUiState.Success -> {
                OMTeamText(
                    text = dailyFeedbackUiState.data.feedbackText,
                    style = PretendardType.body02_2,
                    color = Gray10
                )
            }
            else -> {
                OMTeamText(
                    text = "피드백 데이터가 없습니다",
                    style = PretendardType.body02_2,
                    color = Gray07
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "ReportScreen - 데이터 없음")
@Composable
private fun ReportScreenPreview() {
    ReportContent(
        weekDisplayText = "이번 주",
        weeklyReportUiState = WeeklyReportUiState.Idle,
        dailyFeedbackUiState = DailyFeedbackUiState.Idle,
        onPreviousWeekClick = {},
        onNextWeekClick = {},
        onWeekSelectClick = {},
        onRefreshClick = {}
    )
}

@Preview(showBackground = true, name = "ReportScreen - 데이터 있음")
@Composable
private fun ReportScreenWithDataPreview() {
    val sampleWeeklyReport = WeeklyReport(
        weekStartDate = LocalDate.now().minusDays(6),
        weekEndDate = LocalDate.now(),
        thisWeekSuccessRate = 75.0,
        lastWeekSuccessRate = 60.0,
        thisWeekSuccessCount = 5,
        dailyResults = listOf(
            DailyResult(LocalDate.now().minusDays(6), DayOfWeek.SUNDAY, DailyMissionStatus.SUCCESS),
            DailyResult(LocalDate.now().minusDays(5), DayOfWeek.MONDAY, DailyMissionStatus.SUCCESS),
            DailyResult(LocalDate.now().minusDays(4), DayOfWeek.TUESDAY, DailyMissionStatus.FAILED),
            DailyResult(LocalDate.now().minusDays(3), DayOfWeek.WEDNESDAY, DailyMissionStatus.SUCCESS),
            DailyResult(LocalDate.now().minusDays(2), DayOfWeek.THURSDAY, DailyMissionStatus.NOT_PERFORMED),
            DailyResult(LocalDate.now().minusDays(1), DayOfWeek.FRIDAY, DailyMissionStatus.SUCCESS),
            DailyResult(LocalDate.now(), DayOfWeek.SATURDAY, DailyMissionStatus.SUCCESS)
        ),
        typeSuccessCounts = emptyList(),
        topFailureReasons = listOf(
            TopFailureReason(1, "시간부족", 3),
            TopFailureReason(2, "체력부족", 2)
        ),
        aiFeedback = AiFeedback(
            failureReasonRanking = emptyList(),
            weeklyFeedback = "잘하고 있어요!"
        )
    )

    val sampleDailyFeedback = DailyFeedback(
        targetDate = LocalDate.now(),
        feedbackText = "오늘도 열심히 운동하셨네요! 꾸준한 노력이 가장 중요해요."
    )

    ReportContent(
        weekDisplayText = "2024년 12월 3주",
        weeklyReportUiState = WeeklyReportUiState.Success(sampleWeeklyReport),
        dailyFeedbackUiState = DailyFeedbackUiState.Success(sampleDailyFeedback),
        onPreviousWeekClick = {},
        onNextWeekClick = {},
        onWeekSelectClick = {},
        onRefreshClick = {},
        onDetailedAnalysisClick = {}
    )
}