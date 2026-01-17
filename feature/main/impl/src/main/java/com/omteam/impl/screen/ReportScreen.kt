package com.omteam.impl.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.viewmodel.MainViewModel
import com.omteam.omt.core.designsystem.R
import timber.log.Timber

@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val weekDisplayText by viewModel.weekDisplayText.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
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
                    .clickable {
                        viewModel.moveToPreviousWeek()
                    }
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
                    .clickable {
                        Timber.d("## 주 선택")
                    }
            )

            Spacer(modifier = Modifier.height(dp12))

            Image(
                painter = painterResource(id = R.drawable.icon_arrow_forth),
                contentDescription = "다음 주",
                modifier = Modifier.size(dp24)
                    .clickable {
                        viewModel.moveToNextWeek()
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.icon_update_able),
                contentDescription = "새로고침",
                modifier = Modifier.size(dp24)
                    .clickable {
                        viewModel.resetToCurrentWeek()
                    }
            )
        }

        Spacer(modifier = Modifier.height(dp102))

        Box(
            modifier = Modifier
                .size(dp175)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .size(dp175)
                    .background(
                        color = ErrorBottomSheetBackground,
                        shape = CircleShape
                    )
            )
        }

        Spacer(modifier = Modifier.height(dp54))
        
        OMTeamText(
            text = stringResource(com.omteam.main.impl.R.string.empty_report_data),
            style = PaperlogyType.headline01,
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportScreenPreview() {
    ReportScreen()
}