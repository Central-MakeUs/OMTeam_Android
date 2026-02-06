# ====================================
# Main Implementation 모듈
# ====================================

# ViewModel 유지
-keep class com.omteam.omt.feature.main.impl.** extends androidx.lifecycle.ViewModel { *; }

# Composable 화면 유지
-keep @androidx.compose.runtime.Composable class com.omteam.omt.feature.main.impl.** { *; }
-keepclassmembers class com.omteam.omt.feature.main.impl.** {
    @androidx.compose.runtime.Composable <methods>;
}

# UiState, UiEvent 모델 유지
-keep class com.omteam.omt.feature.main.impl.model.** { *; }
-keep class com.omteam.omt.feature.main.impl.state.** { *; }