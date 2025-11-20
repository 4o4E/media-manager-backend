import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    idea
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    group = "top.e404"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(project(":system"))
    implementation(project(":media"))
}

idea {
    module.excludeDirs.add(file(".run/logs"))
    module.excludeDirs.add(file(".run/data"))
    module.excludeDirs.add(file(".kotlin"))
}