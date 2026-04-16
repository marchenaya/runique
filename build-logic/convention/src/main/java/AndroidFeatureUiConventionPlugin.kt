import com.marchenaya.convention.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {
    private companion object {
        private const val RUNIQUE_ANDROID_LIBRARY_COMPOSE = "runique.android.library.compose"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(RUNIQUE_ANDROID_LIBRARY_COMPOSE)
            }

            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }

}