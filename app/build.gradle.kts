import java.util.Properties

plugins {
    id("omteam.android.application")
    id("omteam.android.compose")
    id("omteam.android.hilt")
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.omteam.omt"

    defaultConfig {
        applicationId = "com.omteam.omt"
        versionCode = 1
        versionName = "1.0"
        
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"${localProperties.getProperty("KAKAO_NATIVE_KEY", "")}\""
        )
        
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            "\"${localProperties.getProperty("GOOGLE_WEB_CLIENT_ID", "")}\""
        )
        
        // 매니페스트에서 사용
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = localProperties.getProperty("KAKAO_NATIVE_KEY") ?: ""
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // feature 모듈
    implementation(project(":feature:main"))
    implementation(project(":feature:login"))

    // core 모듈
    implementation(project(":core:designsystem"))

    // 필수 의존성
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // 카카오 로그인
    implementation(libs.kakao.user)

    // 구글 로그인 (Credential Manager)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // 파이어베이스
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    
    // timber
    implementation(libs.timber)

    // test
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}