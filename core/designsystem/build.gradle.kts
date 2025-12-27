plugins {
    id("omteam.android.library")
    id("omteam.android.compose")
}

android {
    namespace = "com.omteam.omt.core.designsystem"
}

dependencies {
    // Compose 의존성은 AndroidComposeConventionPlugin에서 자동 추가됨
    
    // Android 의존성
    implementation(libs.androidx.core.ktx)
}