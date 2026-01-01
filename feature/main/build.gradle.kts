plugins {
    id("omteam.android.library")
    id("omteam.android.compose")
    id("omteam.android.hilt")
}

android {
    namespace = "com.omteam.omt.feature.main"
}

dependencies {
    // core 모듈
    implementation(project(":core:presentation"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))

    // Activity Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    
    // Hilt Navigation Compose
    implementation(libs.androidx.hilt.navigation.compose)
    
    // 카카오 SDK (설치 여부 확인용)
    implementation(libs.kakao.user)
}