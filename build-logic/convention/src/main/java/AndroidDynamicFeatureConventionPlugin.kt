import com.android.build.api.dsl.DynamicFeatureExtension
import com.marchenaya.convention.Constants.ORG_JETBRAINS_KOTLIN_PLUGIN_COMPOSE
import com.marchenaya.convention.Constants.TEST
import com.marchenaya.convention.Constants.TEST_IMPLEMENTATION
import com.marchenaya.convention.ExtensionType
import com.marchenaya.convention.addUiLayerDependencies
import com.marchenaya.convention.configureAndroidCompose
import com.marchenaya.convention.configureBuildTypes
import com.marchenaya.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

@Suppress("unused")
class AndroidDynamicFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(ANDROID_DYNAMIC_FEATURE)
                apply(ORG_JETBRAINS_KOTLIN_PLUGIN_COMPOSE)
            }

            extensions.configure<DynamicFeatureExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.DYNAMIC_FEATURE
                )
            }

            dependencies {
                addUiLayerDependencies(target)
                TEST_IMPLEMENTATION(kotlin(TEST))
            }
        }
    }

    private companion object {
        private const val ANDROID_DYNAMIC_FEATURE = "com.android.dynamic-feature"
    }

}