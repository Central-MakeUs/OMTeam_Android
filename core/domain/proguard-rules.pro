# ====================================
# Domain 모듈
# ====================================

# Domain 모델 클래스 유지
-keep class com.omteam.omt.core.domain.model.** { *; }

# Repository 인터페이스 유지
-keep interface com.omteam.omt.core.domain.repository.** { *; }

# UseCase 클래스 유지
-keep class com.omteam.omt.core.domain.usecase.** { *; }

# Result/Response wrapper 클래스 유지
-keep class com.omteam.omt.core.domain.result.** { *; }
-keep class com.omteam.omt.core.domain.common.** { *; }

# ====================================
# Kotlin
# ====================================

# Sealed class 유지
-keep class com.omteam.omt.core.domain.** extends kotlin.Enum { *; }
-keepclassmembers class com.omteam.omt.core.domain.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}