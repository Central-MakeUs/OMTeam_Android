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
@Composable
fun EditFavoriteExerciseScreen(
    modifier: Modifier = Modifier,
    initialFavoriteExercises: List<String> = emptyList(),
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onUpdateSuccess: () -> Unit = {}
) {
    val onboardingInfoState by myPageViewModel.onboardingInfoState.collectAsStateWithLifecycle()

    // 수정 성공 시 뒤로 가기
    LaunchedEffect(onboardingInfoState) {
        if (onboardingInfoState is MyPageOnboardingState.Success) {
            onUpdateSuccess()
        }
    }

    EditFavoriteExerciseContent(
        modifier = modifier,
        initialFavoriteExercises = initialFavoriteExercises,
        isLoading = onboardingInfoState is MyPageOnboardingState.Loading,
        onBackClick = onBackClick,
        onUpdateClick = { exercises ->
            myPageViewModel.updatePreferredExercise(exercises)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditFavoriteExerciseContent(
    modifier: Modifier = Modifier,
    initialFavoriteExercises: List<String> = emptyList(),
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onUpdateClick: (List<String>) -> Unit = {}
) {
    var selectedExercises by remember(initialFavoriteExercises) { mutableStateOf(initialFavoriteExercises) }
    var isAddingCustom by remember { mutableStateOf(false) }
    var customExerciseName by remember { mutableStateOf("") }
    var customExercises by remember { mutableStateOf(listOf<String>()) }

    val availableExercises = listOf(
        stringResource(R.string.walking),
        stringResource(R.string.stretching_yoga),
        stringResource(R.string.home_training),
        stringResource(R.string.health),
        stringResource(R.string.usual_exercise),
    )

    val allExercises = availableExercises + customExercises
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

            SubScreenHeader(
                title = stringResource(R.string.edit_my_info_title),
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(dp28))

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
                                selectedExercises - exercise
                            } else {
                                if (selectedExercises.size < 3) {
                                    selectedExercises + exercise
                                } else {
                                    selectedExercises
                                }
                            }
                        }
                    )
                }

                AddCustomChip(
                    onClick = { isAddingCustom = true },
                    isEditing = isAddingCustom,
                    value = customExerciseName,
                    onValueChange = { customExerciseName = it },
                    onDone = {
                        if (customExerciseName.isNotBlank()) {
                            val trimmedName = customExerciseName.trim()
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

        OMTeamButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp20)
                .padding(bottom = dp20),
            text = stringResource(R.string.edit_favorite_exercise_button),
            enabled = selectedExercises.isNotEmpty() && !isLoading,
            onClick = {
                onUpdateClick(selectedExercises)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditFavoriteExerciseScreenPreview() {
    OMTeamTheme {
        EditFavoriteExerciseContent()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true, name = "선호 운동 선택된 상태")
@Composable
private fun EditFavoriteExerciseScreenWithChipsPreview() {
    OMTeamTheme {
        EditFavoriteExerciseContent(
            initialFavoriteExercises = listOf("생활 속 운동", "스트레칭/요가", "축구")
        )
    }
}