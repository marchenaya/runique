plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.marchenaya.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}