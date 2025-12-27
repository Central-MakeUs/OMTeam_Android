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

    // Activity Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
}