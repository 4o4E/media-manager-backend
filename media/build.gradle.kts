import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":system"))
    // kbson
    implementation("com.github.jershell:kbson:0.5.0")

    // mongodb
    implementation("org.mongodb:mongodb-driver-sync:5.1.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(rootProject)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict"
        )
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.test {
    useJUnitPlatform()
    workingDir = rootDir.resolve(".run").also(File::mkdir)
}