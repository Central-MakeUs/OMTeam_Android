# ====================================
# DataStore Preferences
# ====================================

# DataStore는 자체 ProGuard 규칙을 포함하고 있음
# 추가 경고만 무시
-dontwarn androidx.datastore.preferences.**
-dontwarn com.google.protobuf.**

# ====================================
# DataStore 모듈 클래스
# ====================================

# DataStore 관련 클래스 유지
-keep class com.omteam.omt.core.datastore.** { *; }

# Preferences Keys 유지
-keepclassmembers class com.omteam.omt.core.datastore.** {
    public static final *** INSTANCE;
}