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
import com.omteam.designsystem.component.chip.AddCustomChip
import com.omteam.designsystem.component.chip.SelectableInfoChip
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.EditMyInfoItemWithInfo
import com.omteam.impl.component.SubScreenHeader
import com.omteam.impl.viewmodel.MyPageViewModel
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import com.omteam.omt.core.designsystem.R

/**
 * 선호 운동
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditFavoriteExerciseScreen(
    modifier: Modifier = Modifier,
    initialFavoriteExercises: List<String> = emptyList(), // 이전 화면에서 온보딩 정보로 가져온 선호 운동 값
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onUpdateSuccess: () -> Unit = {}
) {
    // 선택된 선호 운동 목록 (최대 3개)
    // remember(key)로 initialFavoriteExercises가 변경되면 자동으로 상태 업데이트
    var selectedExercises by remember(initialFavoriteExercises) { mutableStateOf(initialFavoriteExercises) }

    // "직접 추가하기" chip 수정 가능 상태
    var isAddingCustom by remember { mutableStateOf(false) }
    var customExerciseName by remember { mutableStateOf("") }

    // 유저가 직접 추가한 운동 리스트
    var customExercises by remember { mutableStateOf(listOf<String>()) }

    // 선택 가능한 기본 운동 리스트
    val availableExercises = listOf(
        stringResource(R.string.walking),
        stringResource(R.string.stretching_yoga),
        stringResource(R.string.home_training),
        stringResource(R.string.health),
        stringResource(R.string.usual_exercise),
    )

    // 기본 운동 리스트 + 커스텀 운동 3개
    val allExercises = availableExercises + customExercises
    
    val onboardingInfoState by myPageViewModel.onboardingInfoState.collectAsStateWithLifecycle()
    
    val scrollState = rememberScrollState()

    // 수정 성공 시 뒤로 가기
    LaunchedEffect(onboardingInfoState) {
        if (onboardingInfoState is MyPageOnboardingState.Success) {
            onUpdateSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 스크롤 가능한 영역
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

            // 선택된 선호 운동 표시
            EditMyInfoItemWithInfo(
                label = stringResource(R.string.choose_favorite_exercises),
                infoMessage = stringResource(R.string.choose_favorite_exercises_info),
                chips = selectedExercises,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(dp52))

            OMTeamText(
                text = stringResource(R.string.favorite_exercises_list),
                style = PretendardType.button03Abled,
                color = Gray10
            )

            Spacer(modifier = Modifier.height(dp20))

            // 선택 가능한 운동 chip 목록
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dp8),
                verticalArrangement = Arrangement.spacedBy(dp8)
            ) {
                allExercises.forEach { exercise ->
                    SelectableInfoChip(
                        text = exercise,
                        isSelected = selectedExercises.contains(exercise),
                        onClick = {
                            selectedExercises = if (selectedExercises.contains(exercise)) {
                                // 이미 선택된 경우 제거
                                selectedExercises - exercise
                            } else {
                                // 최대 3개까지만 선택 가능
                                if (selectedExercises.size < 3) {
                                    selectedExercises + exercise
                                } else {
                                    selectedExercises
                                }
                            }
                        }
                    )
                }

                // 직접 추가하기 chip
                AddCustomChip(
                    onClick = {
                        isAddingCustom = true
                    },
                    isEditing = isAddingCustom,
                    value = customExerciseName,
                    onValueChange = { customExerciseName = it },
                    onDone = {
                        if (customExerciseName.isNotBlank()) {
                            val trimmedName = customExerciseName.trim()
                            // 커스텀 운동 목록에만 추가하고 선택된 목록에는 자동 추가하지 않음
                            if (!customExercises.contains(trimmedName) && !availableExercises.contains(trimmedName)) {
                                customExercises = customExercises + trimmedName
                            }
                        }
                        customExerciseName = ""
                        isAddingCustom = false
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(dp20))
        }

        // 하단 고정 버튼
        OMTeamButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp20)
                .padding(bottom = dp20),
            text = stringResource(R.string.edit_favorite_exercise_button),
            enabled = selectedExercises.isNotEmpty() && onboardingInfoState !is MyPageOnboardingState.Loading,
            onClick = {
                myPageViewModel.updatePreferredExercise(selectedExercises)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditFavoriteExerciseScreenPreview() {
    OMTeamTheme {
        EditFavoriteExerciseScreen()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true, name = "선호 운동 선택된 상태")
@Composable
private fun EditFavoriteExerciseScreenWithChipsPreview() {
    OMTeamTheme {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
            // 스크롤 가능한 영역
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = dp20)
            ) {
                Spacer(modifier = Modifier.height(dp20))
                
                SubScreenHeader(
                    title = stringResource(R.string.edit_my_info_title),
                    onBackClick = {}
                )

                Spacer(modifier = Modifier.height(dp28))

                EditMyInfoItemWithInfo(
                    label = "선호하는 운동을 선택해 주세요.",
                    infoMessage = "최대 3개까지 선택할 수 있어요.",
                    chips = listOf("생활 속 운동", "스트레칭/요가", "축구")
                )

                Spacer(modifier = Modifier.height(dp52))

                // "선호 운동 선택 목록" 텍스트
                OMTeamText(
                    text = "선호 운동 선택 목록",
                    style = PretendardType.button03Abled,
                    color = Gray10
                )

                Spacer(modifier = Modifier.height(dp20))

                // 선택 가능한 운동 chip 목록 (커스텀 추가 포함)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dp8),
                    verticalArrangement = Arrangement.spacedBy(dp8)
                ) {
                    val exercises = listOf(
                        "걷기", "스트레칭/요가", "홈 트레이닝(맨몸 운동)", "헬스", "생활 속 운동",
                        "축구" // 커스텀 추가된 운동
                    )
                    val selected = listOf("생활 속 운동", "스트레칭/요가", "축구")
                    
                    exercises.forEach { exercise ->
                        SelectableInfoChip(
                            text = exercise,
                            isSelected = selected.contains(exercise),
                            onClick = {}
                        )
                    }

                    AddCustomChip(onClick = {})
                }
                
                Spacer(modifier = Modifier.height(dp20))
            }

            // 하단 고정 버튼
            OMTeamButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dp20)
                    .padding(bottom = dp20),
                text = "선호 운동 수정하기",
                enabled = true,
                onClick = {}
            )
        }
    }
}