import com.android.build.api.dsl.LibraryExtension
import com.marchenaya.convention.Constants.ANDROIDX_TEST_RUNNER
import com.marchenaya.convention.Constants.TEST
import com.marchenaya.convention.Constants.TEST_IMPLEMENTATION
import com.marchenaya.convention.ExtensionType
import com.marchenaya.convention.configureBuildTypes
import com.marchenaya.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

@Suppress("unused")
class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(ANDROID_LIBRARY)
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

    private companion object {
        private const val ANDROID_LIBRARY = "com.android.library"
        private const val CONSUMER_RULES_PRO = "consumer-rules.pro"
    }

}