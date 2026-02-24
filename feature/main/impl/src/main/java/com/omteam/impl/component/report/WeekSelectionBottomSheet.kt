package com.omteam.impl.component.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.component.textfield.OMTeamTextField
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.omt.core.designsystem.R
import com.omteam.main.impl.R as MainR

/**
 * 주 선택 바텀 시트
 *
 * @param onDismiss 바텀 시트 닫기 콜백
 * @param onAnalyzeClick "분석 보기" 버튼 클릭 콜백 (year, month, weekOfMonth 전달)
 */
@Composable
fun WeekSelectionBottomSheetContent(
    onDismiss: () -> Unit,
    onAnalyzeClick: (year: String, month: String, weekOfMonth: String) -> Unit = { _, _, _ -> }
) {
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var weekOfMonth by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dp20)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_close),
            contentDescription = stringResource(MainR.string.week_selection_title),
            modifier = Modifier
                .align(Alignment.End)
                .size(dp24)
                .clickable { onDismiss() }
        )

        Spacer(modifier = Modifier.height(dp24))

        OMTeamText(
            text = stringResource(MainR.string.week_selection_title),
            style = PaperlogyType.headline04,
            color = Black
        )

        Spacer(modifier = Modifier.height(dp10))

        OMTeamText(
            text = stringResource(MainR.string.week_selection_subtitle),
            style = PretendardType.body04_4,
            color = Gray09
        )

        Spacer(modifier = Modifier.height(dp36))

        OMTeamText(
            text = stringResource(MainR.string.week_selection_start_date),
            style = PretendardType.button03Abled,
            color = Gray11
        )

        Spacer(modifier = Modifier.height(dp16))

        // 년, 월, 주 입력 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dp12, Alignment.CenterHorizontally)
        ) {
            YearInputField(
                value = year,
                onValueChange = { year = it },
                modifier = Modifier.width(dp108)
            )

            MonthWeekInputField(
                value = month,
                onValueChange = { month = it },
                label = stringResource(MainR.string.week_selection_month_label),
                placeholder = stringResource(MainR.string.week_selection_month_placeholder),
                modifier = Modifier.width(dp84)
            )

            MonthWeekInputField(
                value = weekOfMonth,
                onValueChange = { weekOfMonth = it },
                label = stringResource(MainR.string.week_selection_week_label),
                placeholder = stringResource(MainR.string.week_selection_week_placeholder),
                modifier = Modifier.width(dp84)
            )
        }

        Spacer(modifier = Modifier.height(dp28))

        OMTeamButton(
            text = stringResource(MainR.string.week_selection_button),
            onClick = { onAnalyzeClick(year, month, weekOfMonth) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dp20))
    }
}

@Composable
private fun YearInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OMTeamText(
            text = stringResource(MainR.string.week_selection_year_label),
            style = PretendardType.button02Disabled,
            color = Gray10
        )

        Spacer(modifier = Modifier.height(dp4))

        OMTeamTextField(
            value = value,
            onValueChange = {
                // 숫자만 입력 가능, 최대 4자리
                if (it.all { char -> char.isDigit() } && it.length <= 4) {
                    onValueChange(it)
                }
            },
            placeholder = stringResource(MainR.string.week_selection_year_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MonthWeekInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OMTeamText(
            text = label,
            style = PretendardType.button02Disabled,
            color = Gray10
        )

        Spacer(modifier = Modifier.height(dp4))

        OMTeamTextField(
            value = value,
            onValueChange = {
                // 숫자만 입력 가능, 최대 2자리
                if (it.all { char -> char.isDigit() } && it.length <= 2) {
                    onValueChange(it)
                }
            },
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeekSelectionBottomSheetPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        ) {
            WeekSelectionBottomSheetContent(
                onDismiss = {},
                onAnalyzeClick = { year, month, week ->
                    println("분석 요청 : ${year}년 ${month}월 ${week}주")
                }
            )
        }
    }
}