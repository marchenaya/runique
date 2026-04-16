import com.android.build.api.dsl.LibraryExtension
import com.marchenaya.convention.ExtensionType
import com.marchenaya.convention.configureBuildTypes
import com.marchenaya.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    private companion object {
        private const val COM_ANDROID_LIBRARY = "com.android.library"
        private const val TEST_IMPLEMENTATION = "testImplementation"
        private const val TEST = "test"
        private const val ANDROIDX_TEST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
        private const val CONSUMER_RULES_PRO = "consumer-rules.pro"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(COM_ANDROID_LIBRARY)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig {
                    testInstrumentationRunner = ANDROIDX_TEST_RUNNER
                    consumerProguardFiles(CONSUMER_RULES_PRO)
                }
            }

            dependencies {
                TEST_IMPLEMENTATION(kotlin(TEST))
            }

        }
    }

}