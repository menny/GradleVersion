@file:JvmName("SimpleVersionGeneratorPlugin")

package net.evendanan.versiongenerator

import net.evendanan.versiongenerator.generators.EnvBuildVersionGenerator
import net.evendanan.versiongenerator.generators.StaticVersionGenerator
import org.gradle.api.Plugin
import org.gradle.api.Project

class SimpleVersionGeneratorPlugin : Plugin<Project> {

    override fun apply(project: Project?) {
        project?.run {
            extensions.create("autoVersioning", SimpleConfiguration::class.java)

            afterEvaluate {
                extensions.getByType(SimpleConfiguration::class.java).let { versionConf ->
                    val generators = (if (versionConf.enabled)
                        listOf(EnvBuildVersionGenerator.Generic(versionConf.buildCounterEnvKey, versionConf.buildCounterOffset, versionConf.patchOffset))
                    else
                        emptyList()) + StaticVersionGenerator(1)

                    versionConf.versionData = VersionGeneratorFactory().generateVersion(versionConf.major, versionConf.minor, versionConf.patchOffset, generators).also { verData ->
                        println("Generated version ${verData.versionName} (version-code ${verData.versionCode}). Using ${verData.generator.name} for versioning.")

                        project.version = verData.versionName
                        project.allprojects.forEach { it.version = verData.versionName }
                    }
                }
            }
        }
    }

    //This class has to be `open` so Gradle will be able to create a Proxy to it.
    open class SimpleConfiguration {
        var enabled = true
        var buildCounterEnvKey = ""
        var buildCounterOffset = 0
        var major = 1
        var minor = 0
        var patchOffset = 0
        var versionData: VersionData? = null
    }
}
