package com.omteam.impl.tab.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.White
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R

/**
 * 선호 운동
 */
@Composable
fun EditFavoriteExerciseScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(dp20)
    ) {
        // 상단 헤더
        SubScreenHeader(
            title = stringResource(R.string.edit_my_info_title),
            onBackClick = onBackClick
        )
    }
}

@Preview
@Composable
private fun EditFavoriteExerciseScreenPreview() {
    OMTeamTheme {
        EditFavoriteExerciseScreen()
    }
}