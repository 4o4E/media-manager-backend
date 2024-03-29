import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.6"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
}

allprojects {
    group = "top.e404"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }
    }
}

dependencies {
    implementation(project(":system"))
    implementation(project(":media"))
    implementation(project(":log4jdbc"))
}