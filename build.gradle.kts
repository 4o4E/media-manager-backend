plugins {
    id("org.springframework.boot") version "3.1.6"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
}

allprojects {
    group = "top.e404"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":media"))
}