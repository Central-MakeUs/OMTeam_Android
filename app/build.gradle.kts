plugins {
    id("omteam.android.application")
    id("omteam.android.hilt")
}

android {
    namespace = "com.omteam.omt"

    defaultConfig {
        applicationId = "com.omteam.omt"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // Feature 모듈
    implementation(project(":feature:main"))

    // Core 모듈
    implementation(project(":core:designsystem"))

    // 필수 의존성
    implementation(libs.androidx.core.ktx)
}