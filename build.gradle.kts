plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.1"
    id ("org.openjfx.javafxplugin") version "0.0.13"
}


group = "com.elegant"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("com.sun.webkit:webview-deps:1.3.2")
    implementation ("org.openjfx:javafx-controls:17")
    implementation ("org.openjfx:javafx-web:17")
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

    javafx {
        version = "11.0.2"
    }
}
