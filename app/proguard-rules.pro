# ====================================
# 기본 설정
# ====================================

# 디버깅을 위한 소스 파일 및 라인 번호 정보 유지 (Crashlytics에서 유용)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Annotation 유지
-keepattributes *Annotation*

# Signature 유지 (제네릭 타입 정보)
-keepattributes Signature

# Exception 정보 유지
-keepattributes Exceptions

# ====================================
# Kotlin
# ====================================

# Kotlin Metadata 유지
-keep class kotlin.Metadata { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Kotlin Serialization
-keepattributes InnerClasses
-keep,includedescriptorclasses class com.omteam.omt.**$$serializer { *; }
-keepclassmembers class com.omteam.omt.** {
    *** Companion;
}
-keepclasseswithmembers class com.omteam.omt.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ====================================
# Hilt / Dagger
# ====================================

# Hilt가 생성하는 클래스들 유지
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Hilt Modules
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory$ViewModelFactoriesEntryPoint { *; }

# ====================================
# Jetpack Compose
# ====================================

# Compose 런타임
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material3.** { *; }

# Composable 함수 유지
-keep @androidx.compose.runtime.Composable class ** { *; }
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable <methods>;
}

# ====================================
# Navigation 3
# ====================================

-keep class androidx.navigation3.** { *; }
-keep class androidx.lifecycle.viewmodel.navigation3.** { *; }
-keepclassmembers class ** implements androidx.navigation3.NavigationDestination {
    *;
}

# ====================================
# Firebase
# ====================================

# Firebase Analytics
-keep class com.google.firebase.analytics.** { *; }
-keep class com.google.android.gms.measurement.** { *; }

# Firebase Crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Firebase 일반
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# ====================================
# Kakao SDK
# ====================================

-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# Kakao SDK의 Gson 사용
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.kakao.sdk.**

# ====================================
# Google Credentials Manager
# ====================================

-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }
-keepclassmembers class * {
    @androidx.credentials.* <methods>;
}

# ====================================
# Timber
# ====================================

-keep class timber.log.** { *; }
-dontwarn org.jetbrains.annotations.**

# ====================================
# BuildConfig 유지
# ====================================

-keep class com.omteam.omt.BuildConfig { *; }
-keep class **.BuildConfig { *; }

# ====================================
# 프로젝트 모델 클래스
# ====================================

# 데이터 클래스 유지 (Serialization 사용)
-keep class com.omteam.omt.**.model.** { *; }
-keep class com.omteam.omt.**.dto.** { *; }
-keep class com.omteam.omt.**.entity.** { *; }

# ====================================
# 최적화 설정
# ====================================

# 과도한 최적화 방지
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# 경고 무시
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**