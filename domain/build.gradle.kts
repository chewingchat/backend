dependencies {
    implementation(project(":common"))
    compileOnly("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // 메시징 오류
    implementation("org.springframework.boot:spring-boot-starter-messaging:3.0.0")
}
tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}