plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.marchenaya.auth.data"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}