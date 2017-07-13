package net.evendanan.versiongenerator.generators

import net.evendanan.versiongenerator.GenerationData
import net.evendanan.versiongenerator.VersionGenerator

class StaticVersionGenerator(private val staticVersionCode: Int)
    : VersionGenerator("StaticVersionGenerator") {
    constructor() : this(1)

    override fun getVersionCode(generationData: GenerationData): Int {
        return staticVersionCode
    }

    override fun isValidForEnvironment(): Boolean {
        return true
    }
}