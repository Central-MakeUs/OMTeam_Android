package com.omteam.impl.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp200
import com.omteam.designsystem.theme.Green07
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.omt.core.designsystem.R

/**
 * 커스텀 스플래시 화면
 * 
 * 앱 시작 시 자동 로그인 체크 중에 표시되는 화면
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Green07),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_splash),
                contentDescription = "OMTeam 스플래시 아이콘",
                modifier = Modifier.size(dp200)
            )

            Spacer(modifier = Modifier.height(dp20))

            Image(
                painter = painterResource(id = R.drawable.logo_typo),
                contentDescription = "OMTeam 로고"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    OMTeamTheme {
        SplashScreen()
    }
}
