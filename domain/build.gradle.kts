dependencies {
    implementation(project(":common"))
    compileOnly("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}