package com.marchenaya.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

private const val STRING = "String"
private const val API_KEY = "API_KEY"
private const val BASE_URL = "BASE_URL"
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
    val baseUrlValue = gradleLocalProperties(rootDir, rootProject.providers).getProperty(BASE_URL)

    when (extensionType) {
        ExtensionType.APPLICATION -> {
            extensions.configure<ApplicationExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey = apiKey, baseUrlValue = baseUrlValue)
                    }
                    release {
                        configureReleaseBuildType(
                            defaultProguardFile = getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE_TXT
                            ),
                            apiKey = apiKey,
                            baseUrlValue = baseUrlValue
                        )
                    }
                }
            }
        }

        ExtensionType.LIBRARY -> {
            extensions.configure<LibraryExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey = apiKey, baseUrlValue = baseUrlValue)
                    }
                    release {
                        configureReleaseBuildType(
                            defaultProguardFile = getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE_TXT
                            ),
                            apiKey = apiKey,
                            baseUrlValue = baseUrlValue
                        )
                    }
                }
            }
        }

        ExtensionType.DYNAMIC_FEATURE -> {
            extensions.configure<DynamicFeatureExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey = apiKey, baseUrlValue = baseUrlValue)
                    }
                    release {
                        configureReleaseBuildType(
                            defaultProguardFile = getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE_TXT
                            ),
                            apiKey = apiKey,
                            baseUrlValue = baseUrlValue
                        )
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(apiKey: String, baseUrlValue: String) {
    buildConfigField(STRING, API_KEY, "\"$apiKey\"")
    buildConfigField(STRING, BASE_URL, "\"$baseUrlValue\"")
}

private fun BuildType.configureReleaseBuildType(
    defaultProguardFile: File,
    apiKey: String,
    baseUrlValue: String
) {
    buildConfigField(STRING, API_KEY, "\"$apiKey\"")
    buildConfigField(STRING, BASE_URL, "\"$baseUrlValue\"")

    isMinifyEnabled = true
    proguardFiles(
        defaultProguardFile,
        PROGUARD_RULES_PRO
    )
}