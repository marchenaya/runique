plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.android.room)
}

android {
    namespace = "com.marchenaya.core.database"
}

dependencies {
    implementation(libs.org.mongodb.bson)
    implementation(libs.timber)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
}