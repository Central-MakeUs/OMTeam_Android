package com.omteam.impl.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.omteam.designsystem.foundation.dp48

@Composable
fun AccountLinkCompleteScreen(
    modifier: Modifier = Modifier,
    onNavigateToOnboarding: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "계정 연동이 완료되었어요!",
        )

        Spacer(modifier = Modifier.height(dp48))

        Button(onClick = onNavigateToOnboarding) {
            Text("시작하기")
        }
    }
}