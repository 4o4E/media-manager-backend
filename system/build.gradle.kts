import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

fun kotlinx(id: String, version: String = "1.9.20") = "org.jetbrains.kotlinx:kotlinx-$id:$version"

dependencies {
    // implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.springframework.boot:spring-boot-starter-mail")
    api("org.springframework.boot:spring-boot-starter-quartz")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-aop")

    // knife4j
    api("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.4.0")
    // mybatis-plus
    api("com.baomidou:mybatis-plus-boot-starter:3.5.4.1")

    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")
    api("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    // runtimeOnly("com.sun.mail:jakarta.mail:2.0.1")

    // serialization
    api(kotlinx("serialization-core-jvm", "1.6.2"))
    api(kotlinx("serialization-json-jvm", "1.6.2"))
    // bcrypt
    implementation("at.favre.lib:bcrypt:0.10.2")

    // mongodb
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")

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
