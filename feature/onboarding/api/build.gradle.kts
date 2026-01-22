plugins {
    id("omteam.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.omteam.onboarding.api"
    compileSdk = 36
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}