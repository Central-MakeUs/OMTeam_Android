# ====================================
# Presentation 모듈
# ====================================

# ViewModel 클래스 유지
-keep class com.omteam.omt.core.presentation.** extends androidx.lifecycle.ViewModel { *; }

# UiState, UiEvent 등의 모델 클래스 유지
-keep class com.omteam.omt.core.presentation.model.** { *; }
-keep class com.omteam.omt.core.presentation.state.** { *; }
-keep class com.omteam.omt.core.presentation.event.** { *; }

# Base 클래스 유지
-keep class com.omteam.omt.core.presentation.base.** { *; }

# Extension 함수 유지
-keep class com.omteam.omt.core.presentation.extension.** { *; }

# ====================================
# Lifecycle
# ====================================

# ViewModel 관련
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class androidx.lifecycle.** { *; }