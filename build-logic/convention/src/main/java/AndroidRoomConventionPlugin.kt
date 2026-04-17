import androidx.room.gradle.RoomExtension
import com.marchenaya.convention.Constants.IMPLEMENTATION
import com.marchenaya.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    private companion object {
        private const val ANDROIDX_ROOM = "androidx.room"
        private const val KSP_PLUGIN = "com.google.devtools.ksp"
        private const val ROOM_RUNTIME = "room.runtime"
        private const val ROOM_KTX = "room.ktx"
        private const val KSP = "ksp"
        private const val ROOM_COMPILER = "room.compiler"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(ANDROIDX_ROOM)
                apply(KSP_PLUGIN)
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                IMPLEMENTATION(libs.findLibrary(ROOM_RUNTIME).get())
                IMPLEMENTATION(libs.findLibrary(ROOM_KTX).get())
                KSP(libs.findLibrary(ROOM_COMPILER).get())
            }

        }
    }

}