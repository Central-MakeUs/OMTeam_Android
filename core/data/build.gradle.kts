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
    
    // android 의존성
    implementation(libs.androidx.core.ktx)
    
    // 카카오 로그인
    implementation(libs.kakao.user)

    // 구글 로그인
    implementation(libs.google.play.services.auth)
}