import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.asciidoctor.jvm.convert")
    id("org.jlleitschuh.gradle.ktlint")
    jacoco
    id("jacoco-report-aggregation")
}

java.sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")

tasks {
    testCodeCoverageReport {

        dependsOn(test)

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it) {
                        exclude(
                            "**/ChewingApplicationKt.class",
                            "**/ChewingApplicationKt\$*.class",
                        )
                    }
                },
            ),
        )
    }
}

dependencies {
    jacocoAggregation(project(":common"))
    jacocoAggregation(project(":api"))
    jacocoAggregation(project(":domain"))
}

allprojects {
    group = "${property("projectGroup")}"
    version = "${property("applicationVersion")}"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.asciidoctor.jvm.convert")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    jacoco {
        toolVersion = "0.8.8"
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("io.github.microutils:kotlin-logging:3.0.5")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        kapt("org.springframework.boot:spring-boot-configuration-processor")
        // 추가
        implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
        implementation("io.jsonwebtoken:jjwt-impl:0.11.5") // for Jackson JSON Processor
        //
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("com.ninja-squad:springmockk:${property("springMockkVersion")}")

        implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
        //
        implementation("org.springframework.boot:spring-boot-starter-websocket")
        // 코루틴
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    }

    tasks {
        bootJar {
            enabled = false
        }
        jar {
            enabled = true
        }
    }

    java.sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
            jvmTarget.set(JvmTarget.valueOf("JVM_${project.property("javaVersion")}"))
        }
    }

    tasks.test {
        useJUnitPlatform()
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        forkEvery = 100
    }
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = false
    }
}
