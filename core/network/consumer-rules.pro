# kotlinx-serialization : @Serializable 어노테이션이 붙은 DTO 클래스의 직렬화기 보존
# R8 full mode에서 companion object, KSerializer가 제거될 수 있어 명시적 유지 필요
-keepclassmembers @kotlinx.serialization.Serializable class com.omteam.network.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.omteam.network.**$$serializer { *; }

# 레트로핏 : @retrofit2.http.* 어노테이션이 있는 API 인터페이스 보존 (R8 full mode)
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
# suspend 함수 컴파일 시 Continuation<T> 파라미터로 변환되기 때문에 Continuation 클래스 보존 필요
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
# Response<T> 래퍼를 리턴하는 API 메서드를 위한 보존
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# OkHttp3 : TLS 구현 라이브러리 관련 경고 억제 (consumer 앱으로 전파)
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**