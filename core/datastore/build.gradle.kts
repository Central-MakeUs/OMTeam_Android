plugins {
    id("omteam.android.library")
}

android {
    namespace = "com.omteam.omt.core.datastore"
}

dependencies {
    // dataStore
    implementation(libs.androidx.datastore.preferences)
    
    // hilt
    implementation(libs.hilt.android)
    
    // android 의존성
    implementation(libs.androidx.core.ktx)
}