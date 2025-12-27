plugins {
    id("omteam.android.application")
    id("omteam.android.compose")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}