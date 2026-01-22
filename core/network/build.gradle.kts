import java.util.Properties

plugins {
    id("omteam.android.library")
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
    namespace = "com.omteam.omt.core.network"
    
    defaultConfig {
        val baseUrl = localProperties.getProperty("BASE_URL")
            ?: throw GradleException(
                """
                BASE_URL이 local.properties에 정의되지 않았습니다
                local.properties 파일에 다음을 추가하세요
                BASE_URL=http://server-url/
                """.trimIndent()
            )
        
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }
    
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // domain 모듈
    implementation(project(":core:domain"))
    
    // datastore 모듈 (TokenInterceptor에서 사용)
    implementation(project(":core:datastore"))
    
    // retrofit, okhttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)
    
    // android 의존성
    implementation(libs.androidx.core.ktx)
    
    // timber
    implementation(libs.timber)
}