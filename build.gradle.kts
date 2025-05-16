import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    `kotlin-dsl`
    signing
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.gradleGitVersioning)
    alias(libs.plugins.gradleMavenPublishBase)
    alias(libs.plugins.spotlessGradle)
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
    implementation(libs.spotlessGradlePlugin)
    // Workaround for https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

java { toolchain.languageVersion.set(JavaLanguageVersion.of(11)) }

gitVersioning.apply {
    refs {
        branch(".+") {
            describeTagPattern = "v(?<version>.*)"
            version =
                "\${describe.tag.version:-0.0.0}-\${describe.distance}-\${commit.short}-SNAPSHOT"
        }
        tag("v(?<version>.*)") { version = "\${ref.version}" }
    }
    rev {
        describeTagPattern = "v(?<version>.*)"
        version = "\${describe.tag.version:-0.0.0}-\${describe.distance}-\${commit.short}-SNAPSHOT"
    }
}

spotless {
    ratchetFrom("origin/main")

    kotlinGradle {
        encoding("UTF-8")
        target("**/*.gradle.kts")
        ktfmt().kotlinlangStyle()
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

mavenPublishing {
    description = project.description
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()
    configure(GradlePlugin(javadocJar = JavadocJar.Empty(), sourcesJar = true))
    pomFromGradleProperties()
    pom {
        name.set("gradle-convention-plugins")
        description.set(project.description)
        url.set("https://github.com/opencastsoftware/gradle-convention-plugins")
        inceptionYear.set("2023")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        organization {
            name.set("Opencast Software Europe Ltd")
            url.set("https://opencastsoftware.com")
        }
        developers {
            developer {
                id.set("DavidGregory084")
                name.set("David Gregory")
                organization.set("Opencast Software Europe Ltd")
                organizationUrl.set("https://opencastsoftware.com/")
                timezone.set("Europe/London")
                url.set("https://github.com/DavidGregory084")
            }
        }
        ciManagement {
            system.set("Github Actions")
            url.set("https://github.com/opencastsoftware/gradle-convention-plugins/actions")
        }
        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/opencastsoftware/gradle-convention-plugins/issues")
        }
        scm {
            connection.set(
                "scm:git:https://github.com/opencastsoftware/gradle-convention-plugins.git"
            )
            developerConnection.set(
                "scm:git:git@github.com:opencastsoftware/gradle-convention-plugins.git"
            )
            url.set("https://github.com/opencastsoftware/gradle-convention-plugins")
        }
    }
}

tasks.publish { dependsOn("check") }
