plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.marchenaya.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}