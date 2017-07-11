package net.evendanan.versiongenerator

abstract class VersionGenerator(val name: String) {

    abstract fun isValidForEnvironment(): Boolean

    fun generate(generationData: GenerationData): VersionData {
        return VersionData(getVersionCode(generationData), getVersionName(generationData))
    }

    protected abstract fun getVersionCode(generationData: GenerationData): Int

    protected fun getVersionName(generationData: GenerationData): String {
        return "%d.%d.%d".format(
                generationData.major,
                generationData.minor,
                getVersionCode(generationData) + generationData.patchOffset)
    }
}
