package net.evendanan.versiongenerator

import net.evendanan.versiongenerator.generators.GitBuildVersionGenerator
import net.evendanan.versiongenerator.generators.StaticVersionGenerator
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project?) {
        project!!.extensions.create("versionGenerator", VersionGeneratorFactory::class.java, project)
    }

    //This class has to be `open` so Gradle will be able to create a Proxy to it.
    open class VersionGeneratorFactory(private val project: Project) {

        fun generateVersion(major: Int, minor: Int, patchOffset: Int, generators: Iterable<VersionGenerator>): VersionData {
            val generationData = GenerationData(major, minor, patchOffset)

            for (generator in generators) {
                if (generator.isValidForEnvironment()) {
                    println("Using %s for versioning.".format(generator.name))
                    val versionData = generator.generate(generationData)
                    println("Generated version %s (version-code %d)".format(versionData.versionName, versionData.versionCode))

                    project.version = versionData.versionName
                    return versionData
                }
            }

            throw IllegalStateException("Could not find any valid VersionGenerator for this environment!")

        }

        fun generateVersion(major: Int, minor: Int, patchOffset: Int): VersionData {
            return generateVersion(major, minor, patchOffset,
                    listOf(GitBuildVersionGenerator(),
                            StaticVersionGenerator()))
        }
    }
}
