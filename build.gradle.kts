plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.kotlin.kapt") version "1.6.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.3.2"
}

version = "0.1"
group = "labs.nisaacs"

val kotlinVersion: String by project
val exposedVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")

    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.graphql:micronaut-graphql:3.0.0")
    implementation("com.graphql-java:graphql-java:18.0")
    implementation("jakarta.annotation:jakarta.annotation-api")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    implementation("io.micronaut:micronaut-validation")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.testcontainers:mysql:1.17.1")
    implementation("mysql:mysql-connector-java:8.0.28")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
}

application {
    mainClass.set("labs.nisaacs.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("labs.nisaacs.*")
    }
}


