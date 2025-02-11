import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import util.libs
import util.localProperty
import util.plugin

class AndroidCoreLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(target.libs.plugin("android-library").pluginId)
        target.plugins.apply("maven-publish")
        val base = target.extensions.getByType(BasePluginExtension::class.java)
        val coreExtension = createCoreExtension(target)
        val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.finalizeDsl {
            val artifactName: String = coreExtension.artifactName
            println("artifactName: $artifactName")
            base.archivesName.set(artifactName)

            val versionName: String = coreExtension.versionName
            println("versionName: $versionName")
        }
        applyPublishing(target)
    }

    private fun applyPublishing(target: Project) {
        val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
        val library = target.extensions.getByType(LibraryExtension::class.java)
        val publishing = target.extensions.getByType(PublishingExtension::class.java)
        val coreExtension = target.extensions.getByType(AndroidCoreLibraryExtension::class.java)
        androidComponents.finalizeDsl {
            library.publishing {
                singleVariant(VARIANT) {
                    withSourcesJar()
                    withJavadocJar()
                }
            }
        }
        target.afterEvaluate {
            publishing.publications {
                register(NAME, MavenPublication::class.java) {
                    groupId = GROUP_ID
                    artifactId = coreExtension.artifactName
                    version = coreExtension.versionName
                    from(components.named(VARIANT).get())
                    pom {
                        url.set(URL)
                        licenses {
                            license {
                                name.set("Apache License Version 2.0")
                            }
                        }
                    }
                }
            }
            publishing.repositories {
                maven {
                    name = NAME
                    url = uri(URL)
                    credentials {
                        username = localProperty("gpr.user").get()
                        password = localProperty("gpr.key").get()
                    }
                }
            }
        }
    }

    private fun createCoreExtension(target: Project) = target.extensions.create(
        AndroidCoreLibraryExtension::class.java,
        "core",
        AndroidCoreLibraryExtensionImpl::class.java,
        target,
    )

    companion object {
        private const val NAME = "gpr"
        private const val GROUP_ID = "io.github.u1tramarinet"
        private const val URL = "https://maven.pkg.github.com/u1tramarinet/AndroidCore"
        private const val VARIANT = "release"
    }
}

interface AndroidCoreLibraryExtension {
    var artifactName: String
    var versionName: String
}

open class AndroidCoreLibraryExtensionImpl(project: Project) :
    AndroidCoreLibraryExtension {
    private val _artifactName: Property<String> = project.objects.property(String::class.java)
    private val _versionName: Property<String> = project.objects.property(String::class.java)

    override var artifactName: String
        get() = _artifactName.get()
        set(value) = _artifactName.set(value)

    override var versionName
        get() = _versionName.get()
        set(value) = _versionName.set(value)
}
