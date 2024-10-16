dependencies {
    implementation(project(":common"))
    compileOnly(project(":domain"))
    compileOnly("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
