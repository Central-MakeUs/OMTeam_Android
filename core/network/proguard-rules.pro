# ====================================
# Retrofit
# ====================================

# Retrofit 기본 규칙
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Retrofit 인터페이스와 구현체 유지
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# ====================================
# OkHttp
# ====================================

# OkHttp 플랫폼 관련
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# OkHttp 기본 규칙
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-dontwarn okio.**
-keep class okio.** { *; }

# ====================================
# Kotlinx Serialization
# ====================================

# Kotlinx Serialization 기본 규칙
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Serialization 클래스 유지
-keep,includedescriptorclasses class com.omteam.omt.core.network.**$$serializer { *; }
-keepclassmembers class com.omteam.omt.core.network.** {
    *** Companion;
}
-keepclasseswithmembers class com.omteam.omt.core.network.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Serializable 클래스의 모든 필드 유지
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *;
}

# ====================================
# Network 모듈 클래스
# ====================================

# API 서비스 인터페이스 유지
-keep interface com.omteam.omt.core.network.api.** { *; }

# DTO, Model, Response 클래스 유지
-keep class com.omteam.omt.core.network.model.** { *; }
-keep class com.omteam.omt.core.network.dto.** { *; }

# Interceptor 클래스 유지
-keep class com.omteam.omt.core.network.interceptor.** { *; }

# Authenticator 클래스 유지
-keep class com.omteam.omt.core.network.authenticator.** { *; }

# ====================================
# BuildConfig
# ====================================

-keep class com.omteam.omt.core.network.BuildConfig { *; }