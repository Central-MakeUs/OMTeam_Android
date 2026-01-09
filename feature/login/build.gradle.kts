plugins {
    id("omteam.android.library")
    id("omteam.android.compose")
    id("omteam.android.hilt")
}

android {
    namespace = "com.omteam.login"
    compileSdk = 36
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:presentation"))
    implementation(project(":core:domain"))

    // nav3
    implementation(libs.androidx.navigation3.runtime)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}