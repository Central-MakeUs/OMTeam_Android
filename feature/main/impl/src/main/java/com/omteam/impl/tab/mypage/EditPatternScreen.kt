package com.omteam.impl.tab.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.chip.SelectableInfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.onboarding.LifestyleType
import com.omteam.impl.component.EditMyInfoItemWithInfo
import com.omteam.impl.component.SubScreenHeader
import com.omteam.impl.viewmodel.MyPageViewModel
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import com.omteam.omt.core.designsystem.R

/**
 * 평소 생활 패턴
 */
@Composable
fun EditPatternScreen(
    modifier: Modifier = Modifier,
    initialPattern: String = "",
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onUpdateSuccess: () -> Unit = {}
) {
    val onboardingInfoState by myPageViewModel.onboardingInfoState.collectAsStateWithLifecycle()
    
    // 수정 성공 시 뒤로 가기
    LaunchedEffect(onboardingInfoState) {
        if (onboardingInfoState is MyPageOnboardingState.UpdateSuccess) {
            onUpdateSuccess()
        }
    }

    EditPatternContent(
        modifier = modifier,
        initialPattern = initialPattern,
        isLoading = onboardingInfoState is MyPageOnboardingState.Loading,
        onBackClick = onBackClick,
        onUpdateClick = { lifestyleType ->
            myPageViewModel.updateLifestyle(lifestyleType.name)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditPatternContent(
    modifier: Modifier = Modifier,
    initialPattern: String = "",
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onUpdateClick: (LifestyleType) -> Unit = {}
) {
    // 선택된 생활 패턴 (단일 선택)
    var selectedPattern by remember(initialPattern) { mutableStateOf(initialPattern) }

    // 선택 가능한 패턴 목록
    val patternFirst = stringResource(R.string.pattern_first)
    val patternSecond = stringResource(R.string.pattern_second)
    val patternThird = stringResource(R.string.pattern_third)
    val patternFourth = stringResource(R.string.pattern_fourth)
    
    val availablePatterns = listOf(patternFirst, patternSecond, patternThird, patternFourth)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = dp20)
        ) {
            Spacer(modifier = Modifier.height(dp20))

            // 상단 헤더
            SubScreenHeader(
                title = stringResource(R.string.edit_my_info_title),
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(dp28))

            EditMyInfoItemWithInfo(
                label = stringResource(R.string.choose_pattern),
                infoMessage = stringResource(R.string.choose_pattern_info),
                chips = if (selectedPattern.isNotEmpty()) listOf(selectedPattern) else emptyList(),
                onClick = {}
            )

            Spacer(modifier = Modifier.height(dp52))

            OMTeamText(
                text = stringResource(R.string.pattern_list),
                style = PretendardType.button03Abled,
                color = Gray10
            )

            Spacer(modifier = Modifier.height(dp20))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dp8),
                verticalArrangement = Arrangement.spacedBy(dp8)
            ) {
                availablePatterns.forEach { pattern ->
                    SelectableInfoChip(
                        text = pattern,
                        isSelected = selectedPattern == pattern,
                        onClick = {
                            selectedPattern = if (selectedPattern == pattern) {
                                ""
                            } else {
                                pattern
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(dp20))
        }

        OMTeamButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp20)
                .padding(bottom = dp20),
            text = stringResource(R.string.edit_pattern_button),
            enabled = selectedPattern.isNotEmpty() && !isLoading,
            onClick = {
                val lifestyleType = when (selectedPattern) {
                    patternFirst -> LifestyleType.REGULAR_DAYTIME
                    patternSecond -> LifestyleType.IRREGULAR_OVERTIME
                    patternThird -> LifestyleType.SHIFT_NIGHT
                    patternFourth -> LifestyleType.VARIABLE_DAILY
                    else -> LifestyleType.REGULAR_DAYTIME
                }
                onUpdateClick(lifestyleType)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPatternScreenPreview() {
    OMTeamTheme {
        EditPatternContent()
    }
}

@Preview(showBackground = true, name = "패턴 선택된 상태")
@Composable
private fun EditPatternScreenWithSelectionPreview() {
    OMTeamTheme {
        EditPatternContent(
            initialPattern = "비교적 규칙적인 평일 주간 근무에요."
        )
    }
}