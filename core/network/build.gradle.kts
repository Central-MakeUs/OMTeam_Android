plugins {
    id("omteam.android.library")
    id("omteam.android.hilt")
}

android {
    namespace = "com.omteam.omt.core.network"
}

dependencies {
    // Domain 모듈 (API 응답 → Domain Entity 변환)
    implementation(project(":core:domain"))
    
    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Android 의존성
    implementation(libs.androidx.core.ktx)
}