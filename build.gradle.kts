plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.1"
}

group = "com.elegant"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2022.2.4")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    dependencies{
        implementation( "org.testcontainers:testcontainers:1.17.2")

    }

    publishPlugin {
        token.set("perm:aXBsYWthcw==.OTItNjEyNw==.h04yLcW1ApAHB2YOa4zYJ8aGB3bRf6")
    }
}
