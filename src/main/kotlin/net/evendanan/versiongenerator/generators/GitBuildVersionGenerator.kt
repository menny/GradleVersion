package net.evendanan.versiongenerator.generators

import net.evendanan.versiongenerator.GenerationData
import net.evendanan.versiongenerator.VersionGenerator
import java.io.IOException
import java.util.concurrent.TimeUnit

class GitBuildVersionGenerator(private val buildNumberOffset: Int)
    : VersionGenerator("GitVersionBuilder") {

    constructor() : this(0)

    override fun isValidForEnvironment(): Boolean {
        return getGitHistorySize() > 0
    }

    override fun getVersionCode(generationData: GenerationData): Int {
        val revCount = getGitHistorySize()
        val tagCount = ("git tag".runCommand().split("\n")).size

        return revCount + tagCount + buildNumberOffset
    }

    private companion object {
        fun getGitHistorySize(): Int {
            try {
                return Integer.parseInt("git rev-list --count HEAD --all".runCommand())
            } catch (e: Exception) {
                return -1
            }
        }

        fun String.runCommand(): String {
            try {
                val parts = this.split("\\s".toRegex())
                val proc = ProcessBuilder(*parts.toTypedArray())
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .start()

                proc.waitFor(60, TimeUnit.MINUTES)
                return proc.inputStream.bufferedReader().readText().trim()
            } catch(e: IOException) {
                println("runCommand IOException %s".format(e))
                e.printStackTrace()
                return ""
            }
        }
    }
}