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

# 모든 예외 클래스 보호 (디버깅을 위해)
-keep public class * extends java.lang.Exception { *; }
-keep public class * extends java.lang.Throwable { *; }
-keep public class * extends java.lang.Error { *; }

# Parcelable, Serializable 관련 (Intent 데이터 전달에 필수)
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
    public <fields>;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Reflection 관련 (SDK에서 사용)
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Native 메서드 보호
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# JavaScript Interface 보호 (WebView 사용 시)
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Activity, Service, BroadcastReceiver 보호
-keep public class * extends android.app.Activity { *; }
-keep public class * extends android.app.Service { *; }
-keep public class * extends android.content.BroadcastReceiver { *; }
-keep public class * extends android.content.ContentProvider { *; }

# Activity의 Intent extras를 위한 필드 보호
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
    public void *(android.content.Intent);
}

# Bundle/Intent에 넣는 데이터 클래스 보호
-keepclassmembers class * {
    public <init>(android.os.Bundle);
}

# View 생성자 보호 (Custom View)
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

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

# Compose 런타임 (R8이 기본 규칙을 제공하지만 명시적으로 유지)
-keepclassmembers class androidx.compose.ui.platform.AndroidCompositionLocals_androidKt {
    *** getLocalContext(...);
}

# Composable 함수는 R8이 자동으로 처리하므로 프로젝트 패키지만 명시
-keepclassmembers class com.omteam.omt.** {
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
# Gson (Kakao SDK 및 기타에서 사용)
# ====================================

# Gson 기본 규칙
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# Gson이 사용하는 제네릭 타입 정보 유지
-keepattributes Signature

# Gson으로 직렬화/역직렬화되는 모든 클래스 유지
-keep class * implements com.google.gson.JsonSerializer { *; }
-keep class * implements com.google.gson.JsonDeserializer { *; }
-keep class * implements com.google.gson.InstanceCreator { *; }

# Gson 어노테이션이 있는 필드 유지
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.Expose <fields>;
}

# Gson TypeToken 보호
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken { *; }

# Gson이 리플렉션으로 접근하는 모든 필드 보호
-keepclassmembers class * {
    !transient <fields>;
}

# Gson의 FieldNamingStrategy 보호
-keep class * implements com.google.gson.FieldNamingStrategy { *; }

# ====================================
# Firebase
# ====================================

# Firebase Analytics
-keep class com.google.firebase.analytics.** { *; }
-keep class com.google.android.gms.measurement.** { *; }

# Firebase Crashlytics (SourceFile,LineNumberTable은 이미 위에서 설정됨)
-keep public class * extends java.lang.Exception

# Firebase 일반
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# ====================================
# Kakao SDK
# ====================================

# Kakao SDK 전체 클래스 보호 (리플렉션 사용으로 인해 필수)
-keep class com.kakao.sdk.** { *; }
-keep interface com.kakao.sdk.** { *; }
-keep enum com.kakao.sdk.** { *; }

# Kakao SDK Activity 보호 (매우 중요!)
-keep public class com.kakao.sdk.auth.** { *; }
-keep public class com.kakao.sdk.**.TalkAuthCodeActivity { *; }
-keep public class com.kakao.sdk.**.AuthCodeHandlerActivity { *; }

# Gson TypeAdapter (Kakao SDK에서 사용)
-keep class * extends com.google.gson.TypeAdapter { *; }
-keep class * implements com.google.gson.TypeAdapterFactory { *; }
-keep class * implements com.google.gson.JsonSerializer { *; }
-keep class * implements com.google.gson.JsonDeserializer { *; }

# Kakao SDK 모델 클래스 (Gson 직렬화) - 모든 필드와 메서드 보호
-keep class com.kakao.sdk.**.model.** { *; }
-keep class com.kakao.sdk.**.*Model { *; }
-keep class com.kakao.sdk.**.*Response { *; }
-keep class com.kakao.sdk.**.*Request { *; }

# Kakao SDK 내부 클래스들 (Intent extras에 사용)
-keepclassmembers class com.kakao.sdk.** {
    <init>(...);
    public <methods>;
    public <fields>;
}

-dontwarn com.kakao.sdk.**

# ====================================
# Google Credentials Manager
# ====================================

# Credentials Manager 전체 보호
-keep class androidx.credentials.** { *; }
-keep interface androidx.credentials.** { *; }

# Credentials Manager의 Response/Request 클래스 보호 (매우 중요!)
-keep class androidx.credentials.GetCredentialRequest { *; }
-keep class androidx.credentials.GetCredentialRequest$* { *; }
-keep class androidx.credentials.GetCredentialResponse { *; }
-keep class androidx.credentials.GetCredentialResponse$* { *; }
-keep class androidx.credentials.Credential { *; }
-keep class androidx.credentials.CustomCredential { *; }
-keep class androidx.credentials.exceptions.** { *; }

# Google ID 라이브러리 전체 보호
-keep class com.google.android.libraries.identity.googleid.** { *; }
-keep class com.google.android.libraries.identity.googleid.GoogleIdTokenCredential { *; }
-keep class com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException { *; }

# Play Services Auth 전체 보호 (구글 로그인에 필수)
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.gms.auth.api.** { *; }
-keep class com.google.android.gms.auth.api.identity.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.tasks.** { *; }
-keep class com.google.android.gms.internal.** { *; }

# Parcelable 유지 (Intent로 데이터 전달 시 필요)
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
    public <fields>;
    public <methods>;
}

-keepclassmembers class * {
    @androidx.credentials.* <methods>;
}

# Play Services의 SafeParcelable 보호 (중요!)
-keep class com.google.android.gms.common.internal.safeparcel.SafeParcelable { *; }
-keepclassmembers class * implements com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final ** CREATOR;
    public <fields>;
}

# ====================================
# Timber
# ====================================

# Timber의 Tree 구현체 유지 (커스텀 Tree가 있을 경우)
-keep class * extends timber.log.Timber$Tree { *; }
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
# R8 Full Mode 대응
# ====================================

# R8 Full mode에서 필요한 추가 규칙
# 주의: -assumenosideeffects는 일부 null check를 제거하여 SDK 동작에 문제를 일으킬 수 있으므로 주석 처리
# -assumenosideeffects class kotlin.jvm.internal.Intrinsics {
#     public static void checkNotNull(java.lang.Object);
#     public static void checkNotNull(java.lang.Object, java.lang.String);
#     public static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
#     public static void checkNotNullParameter(java.lang.Object, java.lang.String);
#     public static void checkExpressionValueIsNotNull(java.lang.Object, java.lang.String);
# }

# ====================================
# 최적화 설정
# ====================================

# 과도한 최적화 방지
-optimizationpasses 3
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

# SDK 호환성을 위해 특정 최적화 비활성화
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# 경고 무시
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ====================================
# 보안 강화
# ====================================

# 민감한 정보를 포함할 수 있는 메서드명 난독화
# 주의: -repackageclasses와 -allowaccessmodification은 SDK 동작을 방해할 수 있으므로 주석 처리
# -repackageclasses 'com.omteam.omt'
# -allowaccessmodification