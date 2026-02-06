# ====================================
# Main Implementation 모듈
# ====================================

# ViewModel은 Hilt에서 처리, Composable은 R8이 자동 처리
# UiState, UiEvent 모델 유지 (sealed class/data class)
-keep class com.omteam.omt.feature.main.impl.model.** { *; }
-keep class com.omteam.omt.feature.main.impl.state.** { *; }
-keep class com.omteam.omt.feature.main.impl.event.** { *; }