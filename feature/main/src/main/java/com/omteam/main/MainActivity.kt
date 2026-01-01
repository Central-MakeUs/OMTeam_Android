package com.omteam.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omteam.designsystem.theme.OMTeamTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OMTeamTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    // 상태에 따른 Toast 표시
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is MainViewModel.LoginState.Success -> {
                Toast.makeText(
                    context,
                    "로그인 성공: ${state.userInfo.nickname}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is MainViewModel.LoginState.Error -> {
                Toast.makeText(
                    context,
                    "로그인 실패: ${state.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "카카오 로그인 테스트",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (loginState) {
                is MainViewModel.LoginState.Idle -> "로그인 전"
                is MainViewModel.LoginState.Loading -> "로그인 중..."
                is MainViewModel.LoginState.Success ->
                    "로그인 성공: ${(loginState as MainViewModel.LoginState.Success).userInfo.nickname}"
                is MainViewModel.LoginState.Error ->
                    "로그인 실패: ${(loginState as MainViewModel.LoginState.Error).message}"
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.onLoginStart()
                
                // UI 레이어에서 직접 카카오 로그인 처리
                coroutineScope.launch {
                    val loginResult = if (KakaoLoginManager.isKakaoTalkAvailable(context)) {
                        KakaoLoginManager.loginWithKakaoTalk(context)
                    } else {
                        KakaoLoginManager.loginWithKakaoAccount(context)
                    }
                    
                    loginResult
                        .onSuccess {
                            // 로그인 성공 후 사용자 정보 가져오기
                            viewModel.onLoginSuccess()
                        }
                        .onFailure { error ->
                            viewModel.onLoginFailure(error.message ?: "알 수 없는 오류")
                        }
                }
            },
            enabled = loginState !is MainViewModel.LoginState.Loading
        ) {
            Text(
                text = if (loginState is MainViewModel.LoginState.Loading)
                    "로그인 중..."
                else
                    "카카오 로그인"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}