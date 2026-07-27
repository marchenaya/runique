plugins {
    alias(libs.plugins.runique.android.dynamic.feature)
}
android {
    namespace = "com.marchenaya.analytics.analytics_feature"
}

dependencies {
    implementation(project(":app"))
    implementation(libs.androidx.navigation.compose)

    api(projects.analytics.presentation)
    implementation(libs.androidx.navigation3.runtime)
    implementation(projects.analytics.domain)
    implementation(projects.analytics.data)
    implementation(projects.core.database)
}