import com.marchenaya.convention.Constants.IMPLEMENTATION
import com.marchenaya.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class JvmKtorConventionPlugin : Plugin<Project> {
    private companion object {
        private const val SERIALIZATION = "org.jetbrains.kotlin.plugin.serialization"
        private const val KTOR = "ktor"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply(SERIALIZATION)

            dependencies {
                IMPLEMENTATION(libs.findBundle(KTOR).get())
            }
        }
    }

}