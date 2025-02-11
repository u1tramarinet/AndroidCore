plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        register("Library") {
            id = "io.github.u1tramarinet.library"
            implementationClass = "AndroidCoreLibraryPlugin"
        }
    }
}