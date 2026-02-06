# ====================================
# DesignSystem 모듈
# ====================================

# Compose 관련 클래스 유지
-keep class com.omteam.omt.core.designsystem.** { *; }

# Composable 함수 유지
-keep @androidx.compose.runtime.Composable class com.omteam.omt.core.designsystem.** { *; }
-keepclassmembers class com.omteam.omt.core.designsystem.** {
    @androidx.compose.runtime.Composable <methods>;
}

# Theme, Color, Typography 등 디자인 시스템 구성 요소
-keep class com.omteam.omt.core.designsystem.theme.** { *; }
-keep class com.omteam.omt.core.designsystem.component.** { *; }
-keep class com.omteam.omt.core.designsystem.icon.** { *; }