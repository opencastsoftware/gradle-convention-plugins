# gradle-convention-plugins

[![CI](https://github.com/opencastsoftware/gradle-convention-plugins/actions/workflows/ci.yml/badge.svg)](https://github.com/opencastsoftware/gradle-convention-plugins/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.opencastsoftware.gradle/gradle-convention-plugins)](https://search.maven.org/search?q=g%3Acom.opencastsoftware.gradle+AND+a%3Agradle-convention-plugins)
[![License](https://img.shields.io/github/license/opencastsoftware/gradle-convention-plugins?color=blue)](https://spdx.org/licenses/Apache-2.0.html)

This project hosts convention plugins for Gradle projects at Opencast.

Using convention plugins enables us to reuse build configuration across our JVM open source projects.

There is only one convention plugin at present:

* `com.opencastsoftware.gradle.java-conventions` - this plugin declares shared Gradle build configuration for Java projects.

## Installation

Our *gradle-convention-plugins* are published for Java 11 and above.

Gradle (build.gradle):
```groovy
plugins {
  id 'com.opencastsoftware.gradle.java-conventions' version '0.2.0'
}
```

Gradle Kotlin DSL (build.gradle.kts):

```kotlin
plugins {
  id("com.opencastsoftware.gradle.java-conventions") version "0.2.0"
}
```

In practice, we typically use Gradle [version catalogs](https://docs.gradle.org/8.14/userguide/centralizing_dependencies.html#sub:using-catalogs) to declare our plugin dependencies:

Version Catalog (gradle/libs.versions.toml):

```toml
[versions]
gradleJavaConventions = "0.2.0"

[plugins]
gradleJavaConventions = { id = "com.opencastsoftware.gradle.java-conventions", version.ref = "gradleJavaConventions" }
```

Gradle (build.gradle.kts):

```kotlin
plugins {
  alias(libs.plugins.gradleJavaConventions)
}
```

## Usage

After declaring a dependency on this plugin, git versioning, Kotlin DSL formatting, license headers and Maven Central publishing will be configured for your Gradle project.

It's necessary to fill in certain properties in your project's build script:

* `repositories` - we don't configure this by default in the Java conventions plugin as you may have more complex needs. A good default is `repositories { mavenCentral() }`.
* `group` - we don't configure this by default in the Java conventions plugin as you may wish to publish to a nested namespace. A good default is `com.opencastsoftware`.
* `description` - you need to provide your own description for your project!
* `java.toolchain.languageVersion` - this controls the Java toolchain version that will be used to build your project. The minimum version supported by this plugin is Java 11, but you may wish to use a newer JDK.
* `mavenPublishing` properties - there are a lot of properties of the Maven POM file that are project-specific, such as developers, project-specific URLs, inception year etc.
* the `options.release` version for Java compile tasks. You can compile your code for older versions Java VM if required. See the [javac](https://docs.oracle.com/en/java/javase/17/docs/specs/man/javac.html#standard-options) documentation and [JEP 247: Compile for Older Platform Versions](https://openjdk.org/jeps/247).

Please see the **prettier4j** project's [build script](https://github.com/opencastsoftware/prettier4j/blob/3159177b4346187e4d5aee0b7a5507aed518377d/build.gradle.kts) for examples of all of the properties above.

## License

All code in this repository is licensed under the Apache License, Version 2.0. See [LICENSE](./LICENSE).
