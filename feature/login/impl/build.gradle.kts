plugins {
    id("omteam.android.library")
    id("omteam.android.compose")
    id("omteam.android.hilt")
}

android {
    namespace = "com.omteam.login.impl"
    compileSdk = 36
}

dependencies {
    implementation(project(":feature:login:api"))

    // core 모듈
    implementation(project(":core:designsystem"))
    implementation(project(":core:presentation"))
    implementation(project(":core:data")) // GetUserInfoUseCase 사용 위해 필요
    implementation(project(":core:domain"))
    implementation(project(":core:datastore")) // 토큰 저장 위해 필요

    // nav3
    implementation(libs.androidx.navigation3.runtime)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)

    // 카카오 SDK
    implementation(libs.kakao.user)

    // 구글 로그인 (Credential Manager)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // timber
    implementation(libs.timber)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}