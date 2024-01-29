package gradle.plugin

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.qure.library")
                apply("com.qure.hilt")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    dataBinding = true
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("api", project(":presentation"))
                add("implementation", project(":domain"))
                add("testImplementation", libs.findLibrary("junit").get())
            }
        }
    }
}
