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
//    runtimeOnly("com.h2database:h2")
    testImplementation("com.h2database:h2")
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
