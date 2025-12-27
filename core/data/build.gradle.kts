plugins {
    id("omteam.android.library")
    id("omteam.android.hilt")
}

android {
    namespace = "com.omteam.omt.core.data"
}

dependencies {
    // 다른 core 모듈 의존성
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    
    // Android 의존성
    implementation(libs.androidx.core.ktx)
}