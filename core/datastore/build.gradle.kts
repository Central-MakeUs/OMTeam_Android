plugins {
    id("omteam.android.library")
}

android {
    namespace = "com.omteam.omt.core.datastore"
}

dependencies {
    // DataStore (Android 전용)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Android 의존성
    implementation(libs.androidx.core.ktx)
}