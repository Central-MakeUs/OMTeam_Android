import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                add("implementation", "com.google.dagger:hilt-android:2.51.1")
                add("kapt", "com.google.dagger:hilt-compiler:2.51.1")

                // hilt testing
                add("androidTestImplementation", "com.google.dagger:hilt-android-testing:2.51.1")
                add("kaptAndroidTest", "com.google.dagger:hilt-android-compiler:2.51.1")

                // hilt viewmodel
                add("implementation", "androidx.hilt:hilt-navigation-compose:1.2.0")
            }
        }
    }
}