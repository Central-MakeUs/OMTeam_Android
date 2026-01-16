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
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.dp12
import com.omteam.designsystem.foundation.dp126
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp200
import com.omteam.designsystem.foundation.dp60
import com.omteam.designsystem.foundation.dp8
import com.omteam.designsystem.foundation.dp9
import com.omteam.designsystem.theme.GreenSub03Button
import com.omteam.designsystem.theme.GreenSub07Button
import com.omteam.designsystem.theme.PaperlogyType

@Composable
fun SetFavoriteExerciseScreen(
    onExerciseChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var selectedFavoriteExercise by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OMTeamText(
                    text = "평소 선호하시는 운동을 선택해주세요.\n(중복 선택 가능)",
                    style = PaperlogyType.headline02
                )

                Spacer(modifier = Modifier.height(dp20))

                OMTeamCard(
                    text = "걷기",
                    isSelected = (selectedFavoriteExercise == "걷기"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedFavoriteExercise = if (selectedFavoriteExercise == "걷기") {
                            ""
                        } else {
                            "걷기"
                        }
                        onExerciseChange(selectedFavoriteExercise)
                    },
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = "스트레칭/요가",
                    isSelected = (selectedFavoriteExercise == "스트레칭/요가"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedFavoriteExercise = if (selectedFavoriteExercise == "스트레칭/요가") {
                            ""
                        } else {
                            "스트레칭/요가"
                        }
                        onExerciseChange(selectedFavoriteExercise)
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = "홈 트레이닝(맨몸 운동)",
                    isSelected = (selectedFavoriteExercise == "홈 트레이닝(맨몸 운동)"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedFavoriteExercise = if (selectedFavoriteExercise == "홈 트레이닝(맨몸 운동)") {
                            ""
                        } else {
                            "홈 트레이닝(맨몸 운동)"
                        }
                        onExerciseChange(selectedFavoriteExercise)
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = "헬스",
                    isSelected = (selectedFavoriteExercise == "헬스"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedFavoriteExercise = if (selectedFavoriteExercise == "헬스") {
                            ""
                        } else {
                            "헬스"
                        }
                        onExerciseChange(selectedFavoriteExercise)
                    }
                )

                Spacer(modifier = Modifier.height(dp12))

                OMTeamCard(
                    text = "생활 속 운동",
                    isSelected = (selectedFavoriteExercise == "생활 속 운동"),
                    textStyle = PaperlogyType.onboardingCardText,
                    onClick = {
                        // 같은 카드 짝수 회 클릭 시 선택 해제
                        selectedFavoriteExercise = if (selectedFavoriteExercise == "생활 속 운동") {
                            ""
                        } else {
                            "생활 속 운동"
                        }
                        onExerciseChange(selectedFavoriteExercise)
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
                    text = "이전",
                    onClick = { onBack() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                Spacer(modifier = Modifier.width(dp9))

                OMTeamButton(
                    text = "다음",
                    onClick = { onNext() },
                    height = dp60,
                    cornerRadius = dp8,
                    modifier = Modifier.width(dp200)
                )
            }
        }
    }

}