package util

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import java.util.Properties

fun Project.localProperty(propertyName: String): Provider<String> = provider {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
        localProperties.getProperty(propertyName)
    } else {
        null
    }
}