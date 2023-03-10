import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.accessors.dm.LibrariesForLibs

// Workaround for https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

plugins {
    java
    jacoco
    signing
    id("com.github.ben-manes.versions")
    id("me.qoomon.git-versioning")
    id("com.vanniktech.maven.publish")
}

repositories {
    mavenCentral()
}

version = "0.0.0-SNAPSHOT"

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
    pom {
        organization {
            name.set("Opencast Software Europe Ltd")
            url.set("https://opencastsoftware.com")
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}