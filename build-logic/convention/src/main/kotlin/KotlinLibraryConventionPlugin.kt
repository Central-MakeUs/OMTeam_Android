import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            extensions.configure(KotlinProjectExtension::class.java) {
                jvmToolchain(17)
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }

            dependencies {
                // 코루틴
                add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

                // JUnit
                add("testImplementation", "junit:junit:4.13.2")
                add("testImplementation", "org.jetbrains.kotlin:kotlin-test")
            }
        }
    }
}