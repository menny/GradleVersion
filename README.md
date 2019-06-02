# Version Generator [![Download](https://api.bintray.com/packages/menny/net.evendanan.autoversion/gradle-plugin/images/download.svg)](https://bintray.com/menny/net.evendanan.autoversion/gradle-plugin/_latestVersion) [![CircleCI](https://circleci.com/gh/menny/GradleVersion/tree/master.svg?style=svg)](https://circleci.com/gh/menny/GradleVersion/tree/master) [![codecov](https://codecov.io/gh/menny/GradleVersion/branch/master/graph/badge.svg)](https://codecov.io/gh/menny/GradleVersion)

A very simple Gradle plugin (and/or a factory method) to generate
version data based on environment status.

## Install

Add the dependency to your `buildscript` block, in your `build.gradle` at the top:

	buildscript {
        repositories {
            ...
            jcenter()
            maven { url "https://plugins.gradle.org/m2/" }
        }
    
        dependencies {
            ...
            classpath 'net.evendanan.autoversion:gradle-plugin:0.1.10'
        }
    }

## Usage - Simple

In your `gradle.build` file apply the plugin:

    apply plugin: 'net.evendanan.autoversion.simple'

Set up the details:

    autoVersioning {
        buildCounterEnvKey = "CI_BUILD_COUNTER"
        major = 1
        minor = 0
    }

Other properties in `autoVersioning`:

 * `enabled` - `true` or `false`, whether to calculate the version values or use a static versioning (always `1`). 
 * `buildCounterEnvKey` - the name of the environment variable which holds the build-counter.
 * `buildCounterOffset` - an integer value to add to the build-counter.
 * `major` - the major value of the version (see https://semver.org/).
 * `minor` - the minor value of the version (see https://semver.org/).
 * `patchOffset` - an offset to add to the patch value of the generated version (see https://semver.org/).


## Usage - Advance

In your `gradle.build` file apply the advance plugin:

    apply plugin: 'net.evendanan.autoversion'
    
Then, generate the version data

    def versionData = versionGenerator.generateVersion(1, 8)
    
This will apply the generated version-name to the `project.version` property.
You may want to use the `versionData` object for other places:

You might want to print out the version info:

    println "Building app with ${versionData.versionName}, code ${versionData.versionCode}."

or, if you are building an Android app, you should also do:

    android {
        defaultConfig {
            versionCode versionData.versionCode
            versionName versionData.versionName
        }
    }
    
## Generators
The way the version-data is generated is by using a `VersionGenerator`. Usually, a generator is specific to a local(?) environment, for example
the `GitBuildVersionGenerator` will look at the number of lines in the git history, which will only work in a project that is a git repo.
Each generator should also say if it can work in the current local environment. This is the return value of `isValidForEnvironment()`.

Currently, built-in support for:
 
 *  `StaticVersionGenerator` - always returns the same version number.
 *  `GitBuildVersionGenerator` - uses the number of commits + tags in the local git repo.
 *  `EnvBuildVersionGenerator.Shippable` - uses `BUILD_NUMBER` environment variable as the version number.
 *  `EnvBuildVersionGenerator.CircleCi` - uses `CIRCLE_BUILD_NUM` environment variable as the version number.
 *  `EnvBuildVersionGenerator.Generic` - you can provide a name to an environment variable which holds the build counter.
 
You can also implement your own generators by extending `VersionGenerator`. You'll need to implement:

    //return `true` is this generator can calculate the version-number at the moment.
    //this could check for existance of an environment variable,
    //or some file-system attribute/property (like git support), etc.
    abstract fun isValidForEnvironment(): Boolean
    
    //return an integer representing the version number. This could be the number of builds
    //done so far, or the number of commits in the source-control system, etc.
    protected abstract fun getVersionCode(generationData: GenerationData): Int

The default generators used are (in order)

    1. EnvBuildVersionGenerator.CircleCi
    2. EnvBuildVersionGenerator.Shippable
    3. GitBuildVersionGenerator
    4. StaticVersionGenerator
    
You can change that (especially, if you have a new generator) by call `generate` which an ordered-list of generators
 to use:
 
    def generators = [
            new net.evendanan.versiongenerator.generators.EnvBuildVersionGenerator.CircleCi(1650, 0),
            new net.evendanan.versiongenerator.generators.GitBuildVersionGenerator(-2268, 0),
            new net.evendanan.versiongenerator.generators.StaticVersionGenerator()
    ]
    
    def versionData = versionGenerator.generateVersion(1, 8, 0, generators)
    
[AnySoftKeyboard](https://github.com/AnySoftKeyboard/AnySoftKeyboard/blob/master/app/build.gradle) is using this plugin.
