plugins {
    id("omteam.android.library")
    id("omteam.android.compose")
}

android {
    namespace = "com.omteam.omt.core.presentation"
}

dependencies {
    // 다른 core 모듈 의존성
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    
    // ViewModel & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // 컴포즈 의존성은 AndroidComposeConventionPlugin에서 자동 추가됨
    
    // android 의존성
    implementation(libs.androidx.core.ktx)
}