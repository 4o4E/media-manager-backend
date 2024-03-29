import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(rootProject)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    test {
        workingDir = rootDir.resolve("run").also(File::mkdir)
    }
}