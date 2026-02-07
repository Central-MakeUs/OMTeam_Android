package com.omteam.impl.tab

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.character.CharacterInfo
import com.omteam.impl.component.mission.*
import com.omteam.impl.viewmodel.enum.AppleStatus
import com.omteam.impl.viewmodel.state.CharacterUiState
import com.omteam.impl.viewmodel.state.DailyAppleData
import com.omteam.impl.viewmodel.state.DailyMissionUiState
import com.omteam.impl.viewmodel.HomeViewModel
import com.omteam.omt.core.designsystem.R
import java.time.LocalDate
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val dailyMissionUiState by homeViewModel.dailyMissionUiState.collectAsStateWithLifecycle()
    val characterUiState by homeViewModel.characterUiState.collectAsStateWithLifecycle()
    val recommendedMissionsUiState by homeViewModel.recommendedMissionsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        homeViewModel.run {
            fetchDailyMissionStatus()
            fetchCharacterInfo()
            requestDailyMissionRecommendations()
        }
    }

    HomeScreenContent(
        dailyMissionUiState = dailyMissionUiState,
        characterUiState = characterUiState,
        weekDays = homeViewModel.getCurrentWeekDays(),
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    dailyMissionUiState: DailyMissionUiState,
    characterUiState: CharacterUiState,
    weekDays: List<DailyAppleData>,
    modifier: Modifier = Modifier
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
                                TextWithTriangle(text = characterUiState.data.encouragementMessage)
                            }

                            else -> {
                                TextWithTriangle(text = "오늘도 힘내세요!")
                            }
                        }

                        Spacer(modifier = Modifier.height(dp20))

                        // TODO : 캐릭터 상태에 따른 이미지 변경
                        Image(
                            painter = painterResource(id = R.drawable.character_normal),
                            contentDescription = "캐릭터 이미지",
                            modifier = Modifier
                                .size(
                                    width = 150.dp,
                                    height = 133.dp
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
                        modifier = Modifier.padding(horizontal = dp20)
                    )
                }

                is DailyMissionUiState.Loading -> MissionLoadingView()
                
                is DailyMissionUiState.Success -> {
                    // currentMission이 null이면 "미션 제안받기", 있으면 "미션 인증하기" 표시
                    RecommendedMissionView(
                        currentMission = dailyMissionUiState.data.currentMission,
                        modifier = Modifier.padding(horizontal = dp20)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dp32),
            ) {
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
                        .padding(vertical = 12.dp)
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

            Spacer(modifier = Modifier.height(dp20))
        }
    }
}

/**
 * 둥근 모서리 박스 안에 텍스트를 표시하고 하단 중앙에 삼각형을 표시하는 컴포저블
 */
@Composable
fun TextWithTriangle(
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