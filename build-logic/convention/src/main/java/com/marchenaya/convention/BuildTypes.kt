package com.marchenaya.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

private const val STRING = "String"
private const val API_KEY = "API_KEY"
private const val BASE_URL = "BASE_URL"
private const val BASE_URL_VALUE = "https://runique.pl-coding.com:8080"
private const val PROGUARD_RULES_PRO = "proguard-rules.pro"

private const val PROGUARD_ANDROID_OPTIMIZE_TXT = "proguard-android-optimize.txt"

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension,
    extensionType: ExtensionType
) {

    commonExtension.buildFeatures.apply {
        buildConfig = true
    }

    val apiKey = gradleLocalProperties(rootDir, rootProject.providers).getProperty(API_KEY)

    when (extensionType) {
        ExtensionType.APPLICATION -> {
            this@configureBuildTypes.extensions.configure<ApplicationExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey)
                    }
                    release {
                        configureReleaseBuildType(
                            defaultProguardFile = getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE_TXT
                            ),
                            apiKey = apiKey
                        )
                    }
                }
            }
        }

        ExtensionType.LIBRARY -> {
            this@configureBuildTypes.extensions.configure<LibraryExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey)
                    }
                    release {
                        configureReleaseBuildType(
                            defaultProguardFile = getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE_TXT
                            ),
                            apiKey = apiKey
                        )
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(apiKey: String) {
    buildConfigField(STRING, API_KEY, "\"$apiKey\"")
    buildConfigField(STRING, BASE_URL, "\"$BASE_URL_VALUE\"")
}

private fun BuildType.configureReleaseBuildType(
    defaultProguardFile: File,
    apiKey: String
) {
    buildConfigField(STRING, API_KEY, "\"$apiKey\"")
    buildConfigField(STRING, BASE_URL, "\"$BASE_URL_VALUE\"")

    isMinifyEnabled = true
    proguardFiles(
        defaultProguardFile,
        PROGUARD_RULES_PRO
    )
}