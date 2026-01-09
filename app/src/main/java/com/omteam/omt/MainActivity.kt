package com.omteam.omt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.omteam.api.LoginNavKey
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.impl.entry.accountLinkCompleteEntry
import com.omteam.impl.entry.loginEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OMTeamTheme {
                val backStackState = remember { mutableStateOf<List<NavKey>>(listOf(LoginNavKey)) }
                val navigator = remember { AppNavigator(backStackState) }

//                LaunchedEffect(backStackState.value) {
//                    Timber.e("## 백스택 : ${backStackState.value.map { it::class.simpleName }}")
//                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDisplay(
                        backStack = backStackState.value,
                        onBack = {
                            navigator.popBackStack()
                        },
                        entryProvider = entryProvider {
                            loginEntry(
                                onNavigateToAccountLinkComplete = {
                                    navigator.navigateToAccountLinkComplete()
                                }
                            )

                            accountLinkCompleteEntry(
                                onNavigateToOnboarding = {
                                    navigator.navigateToOnboarding()
                                }
                            )
                        },
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
    }
}