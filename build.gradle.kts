plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "com.github.nollyak"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}