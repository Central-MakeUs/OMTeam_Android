# ====================================
# Onboarding Implementation 모듈
# ====================================

# ViewModel 유지
-keep class com.omteam.omt.feature.onboarding.impl.** extends androidx.lifecycle.ViewModel { *; }

# Composable 화면 유지
-keep @androidx.compose.runtime.Composable class com.omteam.omt.feature.onboarding.impl.** { *; }
-keepclassmembers class com.omteam.omt.feature.onboarding.impl.** {
    @androidx.compose.runtime.Composable <methods>;
}

# UiState, UiEvent 모델 유지
-keep class com.omteam.omt.feature.onboarding.impl.model.** { *; }
-keep class com.omteam.omt.feature.onboarding.impl.state.** { *; }