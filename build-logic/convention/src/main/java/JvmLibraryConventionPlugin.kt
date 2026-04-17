import com.marchenaya.convention.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {
    private companion object {
        private const val JVM = "org.jetbrains.kotlin.jvm"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply(JVM)

            configureKotlinJvm()
        }
    }

}