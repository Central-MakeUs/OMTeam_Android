import java.util.Properties

plugins {
    id("omteam.android.library")
    id("omteam.android.compose")
    id("omteam.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.omteam.main.impl"
    compileSdk = 36

    defaultConfig {
        val privacyTermsWebviewUrl = localProperties.getProperty("PRIVACY_TERMS_WEBVIEW")
            ?: throw GradleException(
                """
                개인정보 정책 웹뷰 URL이 local.properties에 정의되지 않았습니다
                local.properties 파일에 다음을 추가하세요
                PRIVACY_TERMS_WEBVIEW=url
                """.trimIndent()
            )

        val termsConditionsWebviewUrl = localProperties.getProperty("TERM_CONDITIONS_WEBVIEW")
            ?: throw GradleException(
                """
                이용약관 웹뷰 URL이 local.properties에 정의되지 않았습니다
                local.properties 파일에 다음을 추가하세요
                TERM_CONDITIONS_WEBVIEW=url
                """.trimIndent()
            )

        val noticeWebviewUrl = localProperties.getProperty("NOTICE_WEBVIEW")
            ?: throw GradleException(
                """
                공지사항 URL이 local.properties에 정의되지 않았습니다
                local.properties 파일에 다음을 추가하세요
                NOTICE_WEBVIEW=url
                """.trimIndent()
            )

        val faqWebviewUrl = localProperties.getProperty("FAQ_WEBVIEW")
            ?: throw GradleException(
                """
                FAQ URL이 local.properties에 정의되지 않았습니다
                local.properties 파일에 다음을 추가하세요
                FAQ_WEBVIEW=url
                """.trimIndent()
            )

        buildConfigField("String", "PRIVACY_TERMS_WEBVIEW", "\"$privacyTermsWebviewUrl\"")
        buildConfigField("String", "TERM_CONDITIONS_WEBVIEW", "\"$termsConditionsWebviewUrl\"")
        buildConfigField("String", "NOTICE_WEBVIEW", "\"$noticeWebviewUrl\"")
        buildConfigField("String", "FAQ_WEBVIEW", "\"$faqWebviewUrl\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":feature:main:api"))

    // core 모듈
    implementation(project(":core:designsystem"))
    implementation(project(":core:presentation"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))

    // nav3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.json)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // 파이어베이스 (FCM 토큰 삭제)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    // timber
    implementation(libs.timber)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}