package net.evendanan.versiongenerator.generators

import net.evendanan.versiongenerator.GenerationData
import net.evendanan.versiongenerator.VersionGenerator

abstract class EnvBuildVersionGenerator protected constructor(name: String, private val envKey: String, private val buildNumberOffset: Int)
    : VersionGenerator(name) {

    override fun isValidForEnvironment(): Boolean {
        return System.getenv(envKey)?.isNotBlank() ?: false
    }

    override fun getVersionCode(generationData: GenerationData): Int {
        val buildNumberString = System.getenv(envKey)
        return Integer.parseInt(buildNumberString) + buildNumberOffset
    }

    class CircleCi(buildNumberOffset: Int) : EnvBuildVersionGenerator("CircleCiVersionGenerator", "CIRCLE_BUILD_NUM", buildNumberOffset) {
        constructor() : this(0)
    }

    class Shippable(buildNumberOffset: Int) : EnvBuildVersionGenerator("ShippableVersionGenerator", "BUILD_NUMBER", buildNumberOffset) {
        constructor() : this(0)
    }
}