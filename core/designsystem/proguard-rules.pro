# ====================================
# DesignSystem 모듈
# ====================================

# Composable 함수 유지 (R8이 대부분 자동 처리)
-keepclassmembers class com.omteam.omt.core.designsystem.** {
    @androidx.compose.runtime.Composable <methods>;
}

# Public API 클래스 유지 (다른 모듈에서 사용)
-keep public class com.omteam.omt.core.designsystem.theme.** { *; }
-keep public class com.omteam.omt.core.designsystem.component.** { *; }
-keep public class com.omteam.omt.core.designsystem.icon.** { *; }