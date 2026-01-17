package com.omteam.impl.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.viewmodel.AppleStatus
import com.omteam.impl.viewmodel.DailyAppleData
import com.omteam.impl.viewmodel.MainViewModel
import com.omteam.omt.core.designsystem.R
import java.time.LocalDate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
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
                weekDays = viewModel.getCurrentWeekDays(),
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Gray02)
                    .padding(top = dp20, bottom = dp23)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dp33),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    TextWithTriangle(text = "텍스트 예시 텍스트 예시 텍스트 예시")

                    Spacer(modifier = Modifier.height(dp20))

                    Box(
                        modifier = Modifier
                            .size(dp120)
                            .background(
                                color = ErrorBottomSheetBackground,
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.height(dp9))

                    Box(
                        modifier = Modifier.background(Yellow02)
                            .padding(
                                vertical = dp6,
                                horizontal = dp7
                            )
                            .align(Alignment.Start)
                    ) {
                        OMTeamText(
                            text = "LEVEL 02",
                            style = PretendardType.homeLevelText
                        )
                    }

                    Spacer(modifier = Modifier.height(dp8))
                }
            }
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
                    color = ErrorBottomSheetBackground,
                    shape = RoundedCornerShape(dp4)
                )
                .padding(horizontal = dp20, vertical = dp6),
            contentAlignment = Alignment.Center
        ) {
            OMTeamText(
                text = text,
                style = PretendardType.body05,
                color = Black,
                textAlign = TextAlign.Center
            )
        }

        // 하단 중앙의 삼각형
        Image(
            painter = painterResource(id = R.drawable.bottom_triangle),
            contentDescription = "하단 삼각형",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = dp6) // 텍스트 박스 하단에서 살짝 겹치도록
        )
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
private fun HomeScreenPreview() {
    HomeScreen()
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