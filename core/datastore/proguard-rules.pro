# ====================================
# DataStore Preferences
# ====================================

# DataStore 기본 규칙
-keep class androidx.datastore.*.** { *; }
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

# DataStore Preferences
-keep class androidx.datastore.preferences.** { *; }
-dontwarn androidx.datastore.preferences.**

# Protobuf (DataStore 의존성)
-keep class com.google.protobuf.** { *; }
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