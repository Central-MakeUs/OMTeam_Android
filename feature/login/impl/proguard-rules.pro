# ====================================
# Login Implementation 모듈
# ====================================

# ViewModel 유지
-keep class com.omteam.omt.feature.login.impl.** extends androidx.lifecycle.ViewModel { *; }

# Composable 화면 유지
-keep @androidx.compose.runtime.Composable class com.omteam.omt.feature.login.impl.** { *; }
-keepclassmembers class com.omteam.omt.feature.login.impl.** {
    @androidx.compose.runtime.Composable <methods>;
}

# UiState, UiEvent 모델 유지
-keep class com.omteam.omt.feature.login.impl.model.** { *; }
-keep class com.omteam.omt.feature.login.impl.state.** { *; }