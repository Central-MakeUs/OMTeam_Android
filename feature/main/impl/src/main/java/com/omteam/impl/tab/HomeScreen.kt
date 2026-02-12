package com.omteam.impl.tab

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.character.CharacterInfo
import com.omteam.domain.model.report.AiFeedback
import com.omteam.domain.model.report.TypeSuccessCount
import com.omteam.domain.model.report.WeeklyReport
import com.omteam.impl.component.mission.*
import com.omteam.impl.viewmodel.ChatViewModel
import com.omteam.impl.viewmodel.HomeViewModel
import com.omteam.impl.viewmodel.ReportViewModel
import com.omteam.impl.viewmodel.enum.AppleStatus
import com.omteam.impl.viewmodel.state.CharacterUiState
import com.omteam.impl.viewmodel.state.DailyAppleData
import com.omteam.impl.viewmodel.state.DailyMissionRecommendationUiState
import com.omteam.impl.viewmodel.state.DailyMissionUiState
import com.omteam.impl.viewmodel.state.WeeklyReportUiState
import com.omteam.omt.core.designsystem.R
import timber.log.Timber
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    reportViewModel: ReportViewModel = hiltViewModel(),
    onNavigateToChat: () -> Unit = {}
) {
    val dailyMissionUiState by homeViewModel.dailyMissionUiState.collectAsStateWithLifecycle()
    val characterUiState by homeViewModel.characterUiState.collectAsStateWithLifecycle()
    val dailyMissionRecommendationUiState by homeViewModel.dailyMissionRecommendationUiState.collectAsStateWithLifecycle()
    val weeklyReportUiState by reportViewModel.weeklyReportUiState.collectAsStateWithLifecycle()

    var showMissionRecommendationBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homeViewModel.run {
            fetchDailyMissionStatus()
            fetchCharacterInfo()
        }
        // 주간 리포트 조회 (현재 주 기준)
        reportViewModel.fetchWeeklyReport(useSelectedDate = true)
    }

    HomeScreenContent(
        dailyMissionUiState = dailyMissionUiState,
        characterUiState = characterUiState,
        weeklyReportUiState = weeklyReportUiState,
        weekDays = homeViewModel.getCurrentWeekDays(),
        onRequestMissionClick = {
            // 미션 제안받기 API 호출
            homeViewModel.requestDailyMissionRecommendations()
            showMissionRecommendationBottomSheet = true
        },
        onVerifyMissionClick = { actionType ->
            // 미션 인증하기 API 호출 - type, value, actionType 전송
            chatViewModel.sendMessage(
                type = "TEXT",
                value = "미션 인증",
                actionType = actionType
            )
            // 채팅 탭(1번)으로 이동
            onNavigateToChat()
        },
        modifier = modifier
    )
    
    // 미션 추천 바텀 시트
    if (showMissionRecommendationBottomSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showMissionRecommendationBottomSheet = false
                    }
                }
            },
            sheetState = sheetState,
            containerColor = White,
            shape = RoundedCornerShape(
                topStart = dp32,
                topEnd = dp32
            )
        ) {
            when (val state = dailyMissionRecommendationUiState) {
                is DailyMissionRecommendationUiState.Success -> {
                    MissionRecommendationBottomSheetContent(
                        recommendations = state.data.recommendations,
                        onDismiss = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showMissionRecommendationBottomSheet = false
                                }
                            }
                        },
                        onMissionSelect = { selectedMission ->
                            Timber.d("## 선택한 미션 : ${selectedMission.mission.name}")
                        },
                        onRetryClick = {
                            // 다시 제안받기 - API 재호출
                            Timber.d("## 다시 제안받기 버튼 클릭")
                            homeViewModel.requestDailyMissionRecommendations()
                        },
                        onStartMissionClick = { recommendedMissionId ->
                            // 미션 시작하기 API 호출
                            Timber.d("## 미션 시작하기 버튼 클릭 - recommendedMissionId : $recommendedMissionId")
                            homeViewModel.startMission(recommendedMissionId)
                            // 성공 시 바텀 시트 닫기
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showMissionRecommendationBottomSheet = false
                                }
                            }
                        }
                    )
                }
                is DailyMissionRecommendationUiState.Loading -> {
                    // 로딩 중 표시
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dp40),
                        contentAlignment = Alignment.Center
                    ) {
                        OMTeamText(
                            text = "미션을 불러오는 중...",
                            style = PretendardType.body02_2,
                            color = Gray08
                        )
                    }
                }
                is DailyMissionRecommendationUiState.Error -> {
                    // 에러 표시
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dp40),
                        contentAlignment = Alignment.Center
                    ) {
                        OMTeamText(
                            text = state.message,
                            style = PretendardType.body02_2,
                            color = Error
                        )
                    }
                }
                else -> {
                    // Idle 상태
                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    dailyMissionUiState: DailyMissionUiState,
    characterUiState: CharacterUiState,
    weeklyReportUiState: WeeklyReportUiState,
    weekDays: List<DailyAppleData>,
    modifier: Modifier = Modifier,
    onRequestMissionClick: () -> Unit = {},
    onVerifyMissionClick: (actionType: String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.screen_inner_logo),
                contentDescription = "왼쪽 상단 로고",
                modifier = Modifier
                    .padding(
                        top = dp20,
                        start = dp20,
                    )
                    .size(dp50)
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(dp14))

            // 오늘이 속한 주를 표시
            // 14~20일 중 하나에 속하면 14~20 표시, 21일 되면 21~27 표시
            WeeklyAppleView(
                weekDays = weekDays,
                modifier = Modifier.fillMaxWidth()
            )

            // 화면 크기에 따라 다른 배경 이미지 선택 (600dp 이상이면 태블릿/폴드)
            val configuration = LocalConfiguration.current
            val isTabletOrFold = configuration.screenWidthDp >= 600
            val backgroundImage = if (isTabletOrFold) {
                R.drawable.home_character_background_fold
            } else {
                R.drawable.home_character_background
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // 배경 이미지
                Image(
                    painter = painterResource(id = backgroundImage),
                    contentDescription = "캐릭터 배경",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                // 컨텐츠
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dp20, bottom = dp23)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dp33),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        // 격려 메시지 표시
                        when (characterUiState) {
                            is CharacterUiState.Success -> {
                                CharacterDialogue(text = characterUiState.data.encouragementMessage)
                            }

                            else -> {
                                CharacterDialogue(text = "오늘도 힘내세요!")
                            }
                        }

                        Spacer(modifier = Modifier.height(dp20))

                        // TODO : 캐릭터 상태에 따른 이미지 변경
                        Image(
                            painter = painterResource(id = R.drawable.character_normal),
                            contentDescription = "캐릭터 이미지",
                            modifier = Modifier
                                .size(
                                    width = dp150,
                                    height = dp133
                                )
                                .align(Alignment.CenterHorizontally),
                        )
                    }
                }

                // 배경 이미지 하단 기준 레벨, 프로그레스 바 배치
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(start = dp33, end = dp33, bottom = dp13),
                    horizontalAlignment = Alignment.Start
                ) {
                    // 레벨 표시
                    when (characterUiState) {
                        is CharacterUiState.Success -> {
                            Box(
                                modifier = Modifier
                                    .background(Yellow02)
                                    .padding(
                                        vertical = dp6,
                                        horizontal = dp7
                                    )
                            ) {
                                OMTeamText(
                                    text = "LEVEL ${String.format(Locale.getDefault(), "%02d",
                                        characterUiState.data.level)}",
                                    style = PretendardType.homeLevelText
                                )
                            }
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .background(Yellow02)
                                    .padding(
                                        vertical = dp6,
                                        horizontal = dp7
                                    )
                            ) {
                                OMTeamText(
                                    text = "LEVEL 01",
                                    style = PretendardType.homeLevelText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(dp8))

                    // 진척도 프로그레스 바, 진척도 % 텍스트를 포함한 Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 진척도 프로그레스 바 (배경)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(dp20)
                                .background(
                                    color = Gray05,
                                    shape = RoundedCornerShape(dp32)
                                )
                        ) {
                            // 진행률 표시
                            val progress = when (characterUiState) {
                                is CharacterUiState.Success -> characterUiState.data.experiencePercent / 100f
                                else -> 0f
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(progress)
                                    .background(
                                        color = Green05,
                                        shape = RoundedCornerShape(dp32)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(dp9))

                        // 진척도 %
                        when (characterUiState) {
                            is CharacterUiState.Success -> {
                                OMTeamText(
                                    text = "${characterUiState.data.experiencePercent}%",
                                    style = PretendardType.skipButton.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Black
                                    )
                                )
                            }

                            else -> {
                                OMTeamText(
                                    text = "0%",
                                    style = PretendardType.skipButton.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Black
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dp40))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_mission),
                    contentDescription = "오늘의 미션 아이콘",
                    modifier = Modifier
                        .padding(start = dp20)
                        .size(dp32)
                )

                Spacer(modifier = Modifier.width(dp8))

                OMTeamText(
                    text = "오늘의 미션",
                    style = PaperlogyType.headline02,
                )
            }

            Spacer(modifier = Modifier.height(dp24))

            when (dailyMissionUiState) {
                is DailyMissionUiState.Idle -> {
                    // 초기 상태 - 미션 없는 상태로 표시
                    RecommendedMissionView(
                        currentMission = null,
                        modifier = Modifier.padding(horizontal = dp20),
                        onRequestMissionClick = onRequestMissionClick,
                        onVerifyMissionClick = onVerifyMissionClick
                    )
                }

                is DailyMissionUiState.Loading -> MissionLoadingView()

                is DailyMissionUiState.Success -> {
                    // currentMission이 null이면 "미션 제안받기", 있으면 "미션 인증하기" 표시
                    // 미션 성공 완료 시 버튼 비활성화 및 "내일 다시 만나요!" 표시
                    RecommendedMissionView(
                        currentMission = dailyMissionUiState.data.currentMission,
                        isMissionCompleted = dailyMissionUiState.data.hasCompletedMission,
                        modifier = Modifier.padding(horizontal = dp20),
                        onRequestMissionClick = onRequestMissionClick,
                        onVerifyMissionClick = onVerifyMissionClick
                    )
                }
                
                is DailyMissionUiState.Error -> MissionErrorView(errorMessage = dailyMissionUiState.message)
            }

            Spacer(modifier = Modifier.height(dp64))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_report),
                    contentDescription = "분석 요약 아이콘",
                    modifier = Modifier
                        .padding(start = dp20)
                        .size(dp32)
                )

                Spacer(modifier = Modifier.width(dp8))

                OMTeamText(
                    text = "분석 요약",
                    style = PaperlogyType.headline02,
                )
            }

            Spacer(modifier = Modifier.height(dp24))

            // 주간 리포트 분석 요약 영역
            WeeklyAnalysisSummaryView(
                weeklyReportUiState = weeklyReportUiState,
                modifier = Modifier.padding(start = dp32)
            )

            Spacer(modifier = Modifier.height(dp20))
        }
    }
}

/**
 * 둥근 모서리 박스 안에 캐릭터 대사 표시
 */
@Composable
fun CharacterDialogue(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.wrapContentWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        // 텍스트를 감싼 둥근 박스
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = White,
                    shape = RoundedCornerShape(dp32)
                )
                .padding(horizontal = dp20, vertical = dp6),
            contentAlignment = Alignment.Center
        ) {
            OMTeamText(
                text = text,
                style = PretendardType.body04_2,
                color = Gray11,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 한 주(일~토)의 사과 상태를 표시하는 컴포저블
 */
@Composable
fun WeeklyAppleView(
    weekDays: List<DailyAppleData>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Gray01)
            .padding(
                start = dp20,
                end = dp20,
                top = dp10,
                bottom = dp10
            ),
        horizontalArrangement = Arrangement.spacedBy(dp18)
    ) {
        weekDays.forEach { dayData ->
            DailyAppleItem(
                dayOfMonth = dayData.dayOfMonth,
                status = dayData.status,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 하루의 사과 상태를 표시하는 컴포저블
 *
 * 상단에 사과 이미지, 하단에 날짜(일) 표시
 */
@Composable
fun DailyAppleItem(
    dayOfMonth: Int,
    status: AppleStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 사과 이미지
        Image(
            painter = painterResource(
                id = when (status) {
                    AppleStatus.DEFAULT -> R.drawable.status_default_apple
                    AppleStatus.SUCCESS -> R.drawable.status_success_apple
                    AppleStatus.FAILED -> R.drawable.status_failed_apple
                }
            ),
            contentDescription = "사과 상태",
            modifier = Modifier.size(dp24)
        )

        Spacer(modifier = Modifier.height(dp8))

        // 날짜 텍스트
        OMTeamText(
            text = dayOfMonth.toString(),
            style = PretendardType.body04_2,
            color = Gray08,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 원형 프로그레스 바 컴포저블
 *
 * @param progress 진행률 (0.0 ~ 1.0)
 * @param progressPercent 중앙에 표시할 진행률 텍스트 (23.4%)
 * @param size 원형 프로그레스 바의 크기 (기본값 81dp)
 * @param strokeWidth 원형 선의 두께 (기본값 8dp)
 * @param backgroundColor 배경 원의 색상 (기본값 Gray05)
 * @param progressColor 진행률 표시 색상 (기본값 Green05)
 */
@Composable
fun CircularProgressBar(
    progress: Float,
    progressPercent: String,
    modifier: Modifier = Modifier,
    size: Dp = dp81,
    strokeWidth: Dp = dp8,
    backgroundColor: Color = Gray05,
    progressColor: Color = Green05
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.toPx()
            val canvasHeight = size.toPx()
            val strokeWidthPx = strokeWidth.toPx()
            val diameter = canvasWidth.coerceAtMost(canvasHeight) - strokeWidthPx

            // 배경 원 (회색 트랙)
            drawArc(
                color = backgroundColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(
                    (canvasWidth - diameter) / 2,
                    (canvasHeight - diameter) / 2
                ),
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )

            // 진행률 원 (녹색) - 12시 방향(-90도)부터 시계 방향으로 그리기
            val sweepAngle = (progress * 360).coerceIn(0f, 360f)
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    (canvasWidth - diameter) / 2,
                    (canvasHeight - diameter) / 2
                ),
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }

        // 중앙 진행률 텍스트
        OMTeamText(
            text = progressPercent,
            style = PaperlogyType.circularProgressPercent,
            color = Green09,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 주간 리포트 분석 요약 뷰 컴포저블
 *
 * 원형 프로그레스 바와 주간 성공 개수 및 피드백 텍스트를 표시
 *
 * @param weeklyReportUiState 주간 리포트 UI 상태
 */
@Composable
fun WeeklyAnalysisSummaryView(
    weeklyReportUiState: WeeklyReportUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        when (weeklyReportUiState) {
            is WeeklyReportUiState.Success -> {
                val weeklyReport = weeklyReportUiState.data
                val successRate = (weeklyReport.thisWeekSuccessRate.toFloat() / 100f).coerceIn(0f, 1f)
                // 한 자릿수 소수로 포맷팅 (23.4%)
                val successRatePercent = String.format(Locale.getDefault(), "%.1f%%", weeklyReport.thisWeekSuccessRate)
                val successCount = weeklyReport.thisWeekSuccessCount
                val weeklyFeedback = weeklyReport.aiFeedback.weeklyFeedback ?: "이번 주 미션 분석 데이터를 준비 중이에요!"

                // 원형 프로그레스 바
                CircularProgressBar(
                    progress = successRate,
                    progressPercent = successRatePercent,
                    size = dp81,
                    strokeWidth = dp8,
                    backgroundColor = Gray05,
                    progressColor = Green05
                )

                Spacer(modifier = Modifier.width(dp16))

                // 텍스트 영역 - 원형 프로그레스바 위에서부터 35dp 떨어진 곳에 배치
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = dp35)
                ) {
                    Row {
                        OMTeamText(
                            text = "이번주에 ",
                            style = PaperlogyType.analysisSummaryCountBase,
                            color = Gray11
                        )
                        OMTeamText(
                            text = "미션 ${successCount}개",
                            style = PaperlogyType.analysisSummaryCountHighlight,
                            color = Green09
                        )
                        OMTeamText(
                            text = "를 성공했어요!",
                            style = PaperlogyType.analysisSummaryCountBase,
                            color = Gray11
                        )
                    }

                    Spacer(modifier = Modifier.height(dp8))

                    // 주간 피드백 텍스트
                    OMTeamText(
                        text = weeklyFeedback,
                        style = PretendardType.analysisSummaryFeedback,
                        color = Gray10
                    )
                }
            }

            is WeeklyReportUiState.Loading -> {
                // 로딩 상태 - 회색 원형 프로그레스 바만 표시
                CircularProgressBar(
                    progress = 0f,
                    progressPercent = "--%",
                    size = dp81,
                    strokeWidth = dp8,
                    backgroundColor = Gray05,
                    progressColor = Green05
                )

                Spacer(modifier = Modifier.width(dp16))

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = dp35)
                ) {
                    OMTeamText(
                        text = "분석 데이터 불러오는 중...",
                        style = PaperlogyType.body01,
                        color = Gray08
                    )
                }
            }

            is WeeklyReportUiState.Error -> {
                // 에러 상태
                Box(
                    modifier = Modifier
                        .size(dp81)
                        .background(
                            color = Gray03,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    OMTeamText(
                        text = "!",
                        style = PaperlogyType.headline02,
                        color = Error
                    )
                }

                Spacer(modifier = Modifier.width(dp16))

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = dp35)
                ) {
                    OMTeamText(
                        text = "데이터를 불러오지 못했어요",
                        style = PaperlogyType.body01,
                        color = Gray08
                    )
                }
            }

            else -> {
                // Idle 상태 - 기본 회색 원 표시
                Box(
                    modifier = Modifier
                        .size(dp81)
                        .background(
                            color = ErrorBottomSheetBackground,
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(dp16))

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(vertical = dp12)
                ) {
                    OMTeamText(
                        text = "아직 분석할 데이터가 없어요!",
                        style = PaperlogyType.body01
                    )

                    Spacer(modifier = Modifier.height(dp6))

                    OMTeamText(
                        text = "OMT와 함께 미션을 수행하며 데이터를\n쌓아보아요!",
                        style = PretendardType.body04_3,
                        color = Gray09,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    val weeklyData = listOf(
        DailyAppleData(LocalDate.now(), 14, AppleStatus.DEFAULT),
        DailyAppleData(LocalDate.now(), 15, AppleStatus.SUCCESS),
        DailyAppleData(LocalDate.now(), 16, AppleStatus.FAILED),
        DailyAppleData(LocalDate.now(), 17, AppleStatus.DEFAULT),
        DailyAppleData(LocalDate.now(), 18, AppleStatus.SUCCESS),
        DailyAppleData(LocalDate.now(), 19, AppleStatus.DEFAULT),
        DailyAppleData(LocalDate.now(), 20, AppleStatus.FAILED)
    )

    val sampleWeeklyReport = WeeklyReport(
        weekStartDate = LocalDate.now().minusDays(6),
        weekEndDate = LocalDate.now(),
        thisWeekSuccessRate = 71.4,
        lastWeekSuccessRate = 60.0,
        thisWeekSuccessCount = 5,
        dailyResults = emptyList(),
        typeSuccessCounts = listOf(
            TypeSuccessCount("EXERCISE", "운동", 3),
            TypeSuccessCount("NUTRITION", "영양", 2)
        ),
        topFailureReasons = emptyList(),
        aiFeedback = AiFeedback(
            failureReasonRanking = null,
            weeklyFeedback = "운동 미션을 꾸준히 수행하셨네요! 다음 주도 화이팅!"
        )
    )

    HomeScreenContent(
        dailyMissionUiState = DailyMissionUiState.Idle,
        characterUiState = CharacterUiState.Success(
            data = CharacterInfo(
                level = 5,
                experiencePercent = 65,
                successCount = 5,
                successCountUntilNextLevel = 10,
                encouragementTitle = "힘내세요!",
                encouragementMessage = "오늘도 화이팅!"
            )
        ),
        weeklyReportUiState = WeeklyReportUiState.Success(sampleWeeklyReport),
        weekDays = weeklyData
    )
}

@Preview(showBackground = true)
@Composable
private fun WeeklyAppleViewPreview() {
    val sampleData = listOf(
        DailyAppleData(LocalDate.now(), 14, AppleStatus.DEFAULT),
        DailyAppleData(LocalDate.now(), 15, AppleStatus.SUCCESS),
        DailyAppleData(LocalDate.now(), 16, AppleStatus.FAILED),
        DailyAppleData(LocalDate.now(), 17, AppleStatus.DEFAULT),
        DailyAppleData(LocalDate.now(), 18, AppleStatus.SUCCESS),
        DailyAppleData(LocalDate.now(), 19, AppleStatus.DEFAULT),
        DailyAppleData(LocalDate.now(), 20, AppleStatus.FAILED)
    )
    WeeklyAppleView(weekDays = sampleData)
}

@Preview(showBackground = true)
@Composable
private fun CircularProgressBarPreview() {
    Column(
        modifier = Modifier.padding(dp16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dp16)
    ) {
        // 0% 진행률
        CircularProgressBar(progress = 0f, progressPercent = "0.0%")
        // 50% 진행률
        CircularProgressBar(progress = 0.5f, progressPercent = "50.0%")
        // 75% 진행률
        CircularProgressBar(progress = 0.75f, progressPercent = "75.0%")
        // 100% 진행률
        CircularProgressBar(progress = 1f, progressPercent = "100.0%")
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyAnalysisSummaryViewPreview() {
    val sampleWeeklyReport = WeeklyReport(
        weekStartDate = LocalDate.now().minusDays(6),
        weekEndDate = LocalDate.now(),
        thisWeekSuccessRate = 71.4,
        lastWeekSuccessRate = 60.0,
        thisWeekSuccessCount = 5,
        dailyResults = emptyList(),
        typeSuccessCounts = listOf(
            TypeSuccessCount("EXERCISE", "운동", 3),
            TypeSuccessCount("NUTRITION", "영양", 2)
        ),
        topFailureReasons = emptyList(),
        aiFeedback = AiFeedback(
            failureReasonRanking = null,
            weeklyFeedback = "운동 미션을 꾸준히 수행하셨네요! 다음 주도 화이팅!"
        )
    )

    Column(
        modifier = Modifier.padding(dp16),
        verticalArrangement = Arrangement.spacedBy(dp16)
    ) {
        // 성공 상태
        WeeklyAnalysisSummaryView(
            weeklyReportUiState = WeeklyReportUiState.Success(sampleWeeklyReport)
        )
        // 로딩 상태
        WeeklyAnalysisSummaryView(
            weeklyReportUiState = WeeklyReportUiState.Loading
        )
        // 에러 상태
        WeeklyAnalysisSummaryView(
            weeklyReportUiState = WeeklyReportUiState.Error("네트워크 오류")
        )
        // Idle 상태
        WeeklyAnalysisSummaryView(
            weeklyReportUiState = WeeklyReportUiState.Idle
        )
    }
}
