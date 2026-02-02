package com.omteam.impl.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.R

@Composable
fun SetFavoriteExerciseScreen(
    onExerciseChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var selectedExercises by remember { mutableStateOf(setOf<String>()) }

    val walkingText = stringResource(R.string.walking)
    val stretchingYogaText = stringResource(R.string.stretching_yoga)
    val homeTrainingText = stringResource(R.string.home_training)
    val healthText = stringResource(R.string.health)
    val practicalExerciseText = stringResource(R.string.practical_exercise)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 제목은 스크롤되지 않음
            OMTeamText(
                text = stringResource(R.string.set_favorite_exercise_screen_title),
                style = PaperlogyType.headline02
            )

            Spacer(modifier = Modifier.height(dp20))

            // 카드들만 스크롤 가능
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OMTeamCard(
                    text = walkingText,
                    isSelected = selectedExercises.contains(walkingText),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        selectedExercises = if (selectedExercises.contains(walkingText)) {
                            selectedExercises - walkingText
                        } else {
                            selectedExercises + walkingText
                        }
                        onExerciseChange(selectedExercises.joinToString(", "))
                    },
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = stretchingYogaText,
                    isSelected = selectedExercises.contains(stretchingYogaText),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        selectedExercises = if (selectedExercises.contains(stretchingYogaText)) {
                            selectedExercises - stretchingYogaText
                        } else {
                            selectedExercises + stretchingYogaText
                        }
                        onExerciseChange(selectedExercises.joinToString(", "))
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = homeTrainingText,
                    isSelected = selectedExercises.contains(homeTrainingText),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        selectedExercises = if (selectedExercises.contains(homeTrainingText)) {
                            selectedExercises - homeTrainingText
                        } else {
                            selectedExercises + homeTrainingText
                        }
                        onExerciseChange(selectedExercises.joinToString(", "))
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = healthText,
                    isSelected = selectedExercises.contains(healthText),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        selectedExercises = if (selectedExercises.contains(healthText)) {
                            selectedExercises - healthText
                        } else {
                            selectedExercises + healthText
                        }
                        onExerciseChange(selectedExercises.joinToString(", "))
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = practicalExerciseText,
                    isSelected = selectedExercises.contains(practicalExerciseText),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        selectedExercises = if (selectedExercises.contains(practicalExerciseText)) {
                            selectedExercises - practicalExerciseText
                        } else {
                            selectedExercises + practicalExerciseText
                        }
                        onExerciseChange(selectedExercises.joinToString(", "))
                    }
                )

                // 버튼과 안 겹치게 여백 추가
                Spacer(modifier = Modifier.height(dp20))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dp20),
                horizontalArrangement = Arrangement.Center,
            ) {
                OMTeamButton(
                    text = stringResource(R.string.before),
                    onClick = { onBack() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                Spacer(modifier = Modifier.width(dp9))

                OMTeamButton(
                    text = stringResource(R.string.next),
                    onClick = {
                        if (selectedExercises.isNotEmpty()) {
                            onNext()
                        }
                    },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = if (selectedExercises.isNotEmpty()) Green07 else Green04,
                    modifier = Modifier.width(dp200)
                )
            }
        }
    }

}