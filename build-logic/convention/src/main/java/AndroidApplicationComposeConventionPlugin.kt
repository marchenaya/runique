import com.android.build.api.dsl.ApplicationExtension
import com.marchenaya.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    private companion object {
        private const val RUNIQUE_ANDROID_APPLICATION = "runique.android.application"
        private const val ORG_JETBRAINS_KOTLIN_PLUGIN_COMPOSE =
            "org.jetbrains.kotlin.plugin.compose"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply(RUNIQUE_ANDROID_APPLICATION)
            pluginManager.apply(ORG_JETBRAINS_KOTLIN_PLUGIN_COMPOSE)

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}