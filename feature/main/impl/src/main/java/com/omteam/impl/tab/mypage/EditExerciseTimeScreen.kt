package com.omteam.impl.tab.mypage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R

/**
 * 운동 가능 시간
 */
@Composable
fun EditExerciseTimeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    // 상단 헤더
    SubScreenHeader(
        title = stringResource(R.string.edit_my_info_title),
        onBackClick = onBackClick
    )
}

@Preview(showBackground = true)
@Composable
private fun EditExerciseTimeScreenPreview() {
    OMTeamTheme {
        EditExerciseTimeScreen()
    }
}