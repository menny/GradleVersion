package net.evendanan.versiongenerator.generators

import net.evendanan.versiongenerator.GenerationData
import net.evendanan.versiongenerator.VersionGenerator

class StaticVersionGenerator
    : VersionGenerator("StaticVersionGenerator") {
    override fun getVersionCode(generationData: GenerationData): Int {
        return generationData.patchOffset + 1
    }

    override fun isValidForEnvironment(): Boolean {
        return true
    }
}