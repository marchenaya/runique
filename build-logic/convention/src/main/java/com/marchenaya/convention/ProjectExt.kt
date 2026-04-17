package com.marchenaya.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

private const val LIBS = "libs"

val Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named(LIBS)