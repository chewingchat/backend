plugins {
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    jacoco
    id("jacoco-report-aggregation")
}


dependencies {
    jacocoAggregation(project(":common"))
    jacocoAggregation(project(":api"))
    jacocoAggregation(project(":domain"))

}


allprojects {

    group = "org.chewing"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("io.github.microutils:kotlin-logging:3.0.5")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-web")
        //

        // 추가
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("io.jsonwebtoken:jjwt-jackson:0.11.5") // for Jackson JSON Processor
        //
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        // 웹소켓, 메시지 추가
        implementation("org.springframework.boot:spring-boot-starter-websocket")
        implementation("org.springframework.boot:spring-boot-starter-messaging")
        // redis 관련 추가
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        // curator
        implementation("org.apache.curator:curator-framework:5.4.0")
        implementation("org.apache.curator:curator-recipes:5.4.0")
        implementation("org.apache.zookeeper:zookeeper:3.7.0")  // Zookeeper 라이브러리 추가
        // mongo
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
        //
        implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
        implementation("org.springframework.security:spring-security-oauth2-client")
        //



    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    tasks.test {
        useJUnitPlatform()
        maxParallelForks = 4
        forkEvery = 100
    }
}

subprojects {
    apply(plugin = "jacoco")

    jacoco {
        toolVersion = "0.8.8"
    }
}

tasks.test {
    finalizedBy(tasks.testCodeCoverageReport)
}

tasks.testCodeCoverageReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
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
