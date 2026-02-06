plugins {
    id("omteam.android.library")
    id("omteam.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.omteam.omt.core.database"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
