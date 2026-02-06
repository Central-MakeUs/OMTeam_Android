# ====================================
# Kakao SDK
# ====================================

# Kakao SDK 전체 보호 (리플렉션 사용)
-keep class com.kakao.sdk.** { *; }
-keep interface com.kakao.sdk.** { *; }
-keep enum com.kakao.sdk.** { *; }

# Gson TypeAdapter (Kakao SDK에서 사용)
-keep class * extends com.google.gson.TypeAdapter { *; }
-keep class * implements com.google.gson.TypeAdapterFactory { *; }
-keep class * implements com.google.gson.JsonSerializer { *; }
-keep class * implements com.google.gson.JsonDeserializer { *; }

-dontwarn com.kakao.sdk.**

# ====================================
# Google Credentials Manager
# ====================================

# Credentials Manager 전체 보호
-keep class androidx.credentials.** { *; }
-keep interface androidx.credentials.** { *; }

# Google ID 라이브러리 전체 보호
-keep class com.google.android.libraries.identity.googleid.** { *; }

# Play Services Auth 전체 보호
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.tasks.** { *; }
-keep class com.google.android.gms.internal.** { *; }

# Parcelable 유지
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembers class * {
    @androidx.credentials.* <methods>;
}

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

# Gson 기본 규칙
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# Gson 타입 정보 유지
-keepattributes Signature

# Gson TypeToken
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Gson 어노테이션이 있는 필드 유지
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Enum 유지
-keepclassmembers enum * {
    **[] $VALUES;
    public *;
}