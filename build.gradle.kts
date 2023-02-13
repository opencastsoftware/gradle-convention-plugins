import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    `kotlin-dsl`
    signing
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.gradleGitVersioning)
    alias(libs.plugins.gradleMavenPublishBase)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

group = "com.opencastsoftware.gradle"
description = "Convention plugins for Gradle builds at Opencast"
version = "0.0.0-SNAPSHOT"

dependencies {
    implementation(libs.gradleVersionsPlugin)
    implementation(libs.gradleGitVersioningPlugin)
    implementation(libs.gradleMavenPublishPlugin)
    // Workaround for https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

gitVersioning.apply {
    refs {
        branch(".+") {
            describeTagPattern = "v(?<version>.*)"
            version = "\${describe.tag.version:-0.0.0}-\${describe.distance}-\${commit.short}-SNAPSHOT"
        }
        tag("v(?<version>.*)") {
            version = "\${ref.version}"
        }
    }
    rev {
        describeTagPattern = "v(?<version>.*)"
        version = "\${describe.tag.version:-0.0.0}-\${describe.distance}-\${commit.short}-SNAPSHOT"
    }
}

mavenPublishing {
    description = project.description
    publishToMavenCentral(SonatypeHost.S01, true)
    signAllPublications()
    configure(JavaLibrary(JavadocJar.Empty()))
    pomFromGradleProperties()
    pom {
        description.set(project.description)
        inceptionYear.set("2023")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("DavidGregory084")
                name.set("David Gregory")
                url.set("https://github.com/DavidGregory084")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/opencastsoftware/gradle-convention-plugins.git")
            developerConnection.set("scm:git:git@github.com:opencastsoftware/gradle-convention-plugins.git")
            url.set("https://github.com/opencastsoftware/gradle-convention-plugins")
        }
    }
}

tasks.publish {
    dependsOn("check")
}