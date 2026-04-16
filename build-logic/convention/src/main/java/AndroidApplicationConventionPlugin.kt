import com.android.build.api.dsl.ApplicationExtension
import com.marchenaya.convention.configureKotlinAndroid
import com.marchenaya.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    private companion object {
        private const val ANDROID_APPLICATION = "com.android.application"
        private const val PROJECT_APPLICATION_ID = "projectApplicationId"
        private const val PROJECT_VERSION_SDK = "projectTargetSdkVersion"
        private const val PROJECT_VERSION_CODE = "projectVersionCode"
        private const val PROJECT_VERSION_NAME = "projectVersionName"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(ANDROID_APPLICATION)
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion(PROJECT_APPLICATION_ID).get().toString()
                    targetSdk = libs.findVersion(PROJECT_VERSION_SDK).get().toString().toInt()

                    versionCode = libs.findVersion(PROJECT_VERSION_CODE).get().toString().toInt()
                    versionName = libs.findVersion(PROJECT_VERSION_NAME).get().toString()
                }

                configureKotlinAndroid(this)

            }
        }
    }

}