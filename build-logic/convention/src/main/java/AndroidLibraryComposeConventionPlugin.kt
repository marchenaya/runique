import com.android.build.api.dsl.LibraryExtension
import com.marchenaya.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    private companion object {
        private const val RUNIQUE_ANDROID_LIBRARY = "runique.android.library"
        private const val ORG_JETBRAINS_KOTLIN_PLUGIN_COMPOSE =
            "org.jetbrains.kotlin.plugin.compose"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(RUNIQUE_ANDROID_LIBRARY)
                apply(ORG_JETBRAINS_KOTLIN_PLUGIN_COMPOSE)
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }

}