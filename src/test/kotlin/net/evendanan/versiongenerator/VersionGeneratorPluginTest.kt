package net.evendanan.versiongenerator

import com.nhaarman.mockito_kotlin.*
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.junit.Test

class VersionGeneratorPluginTest {

    @Test
    fun testFactoryExistsInExtension() {
        val underTest = VersionGeneratorPlugin()
        val project = mock<Project>()
        val extensions = mock<ExtensionContainer>()
        doReturn(extensions).`when`(project).extensions

        underTest.apply(project)

        verify(extensions).create(eq("versionGenerator"), eq(VersionGeneratorPlugin.Factory::class.java), same(project))
    }

    @Test
    fun testDoesNotCrashOnNullApply() {
        val underTest = VersionGeneratorPlugin()

        underTest.apply(null)
    }

    @Test
    fun testFactorySetsVersionName() {
        val project = mock<Project>()
        val underTest = VersionGeneratorPlugin.Factory(project)

        underTest.generateVersion(1, 2, 3, listOf(VersionGeneratorFactoryTest.FakeVersionGenerator("first", true, 2)))

        verify(project).version = "1.2.5"
    }

    @Test
    fun testFactorySetsVersionNameWithDefaultGenerators() {
        val project = mock<Project>()
        val underTest = VersionGeneratorPlugin.Factory(project)

        underTest.generateVersion(2, 24, 2)

        verify(project).version = any<String>()
    }
}