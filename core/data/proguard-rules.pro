# ====================================
# Kakao SDK
# ====================================

# Kakao SDK 기본 규칙
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# Kakao SDK의 내부 클래스 유지
-keep class com.kakao.sdk.auth.** { *; }
-keep class com.kakao.sdk.user.** { *; }
-keep class com.kakao.sdk.common.** { *; }

# Kakao SDK Gson 관련
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.kakao.sdk.**

# Kakao SDK 모델 클래스
-keep class com.kakao.sdk.**.model.** { *; }

# ====================================
# Google Credentials Manager
# ====================================

# Credentials Manager 기본 규칙
-keep class androidx.credentials.** { *; }
-keep interface androidx.credentials.** { *; }

# Google ID 관련
-keep class com.google.android.libraries.identity.googleid.** { *; }
-keepclassmembers class com.google.android.libraries.identity.googleid.** {
    *;
}

# Credentials API 어노테이션
-keepclassmembers class * {
    @androidx.credentials.* <methods>;
}

# Google Play Services Auth
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.gms.common.** { *; }
-dontwarn com.google.android.gms.**

# ====================================
# Data 모듈 Repository
# ====================================

# Repository 인터페이스 및 구현체 유지
-keep interface com.omteam.omt.core.data.repository.** { *; }
-keep class com.omteam.omt.core.data.repository.** { *; }

# Data Source 클래스 유지
-keep class com.omteam.omt.core.data.datasource.** { *; }

# Mapper 클래스 유지
-keep class com.omteam.omt.core.data.mapper.** { *; }

# ====================================
# Gson (Kakao SDK 의존성)
# ====================================

-keepattributes Signature
-keepattributes *Annotation*

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

-keepclassmembers enum * {
    **[] $VALUES;
    public *;
}