plugins {
    kotlin("plugin.jpa") version "2.0.0"
    kotlin("plugin.allopen")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

dependencies {
    implementation(project(":common"))
    compileOnly(project(":domain"))
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // 테스트용
    testImplementation("com.h2database:h2")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.11.0")
    testImplementation(project(":domain"))
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
