# ====================================
# Presentation 모듈
# ====================================

# ViewModel 클래스 유지 (Hilt가 주입)
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# UiState, UiEvent 등의 모델 클래스 유지 (sealed class/interface 포함)
-keep class com.omteam.omt.core.presentation.model.** { *; }
-keep class com.omteam.omt.core.presentation.state.** { *; }
-keep class com.omteam.omt.core.presentation.event.** { *; }

# Base 클래스 유지
-keep public class com.omteam.omt.core.presentation.base.** { *; }