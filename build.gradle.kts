import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    idea
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
    // implementation(project(":media"))
    implementation("ru.vasiand:spring-boot-starter-log4jdbc:1.1.0")
}

idea {
    module.excludeDirs.add(file(".run/logs"))
    module.excludeDirs.add(file(".run/data"))
    module.excludeDirs.add(file(".kotlin"))
}