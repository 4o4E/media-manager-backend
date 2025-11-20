import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

fun kotlinx(id: String, version: String = "2.0.20") = "org.jetbrains.kotlinx:kotlinx-$id:$version"

dependencies {
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.springframework.boot:spring-boot-starter-mail")
    api("org.springframework.boot:spring-boot-starter-quartz")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-aop")

    // knife4j
    api("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.4.0")
    // mybatis-plus
    api("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.7")

    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")
    api("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.postgresql:postgresql:42.7.5")
    // runtimeOnly("com.mysql:mysql-connector-j")
    // runtimeOnly("com.sun.mail:jakarta.mail:2.0.1")

    // serialization
    api(kotlinx("serialization-core-jvm", "1.7.2"))
    api(kotlinx("serialization-json-jvm", "1.7.2"))
    // bcrypt
    implementation("at.favre.lib:bcrypt:0.10.2")

    // p6spy
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.12.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(rootProject)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.test {
    useJUnitPlatform()
    workingDir = rootDir.resolve(".run").also(File::mkdir)
}
