dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation(project(":common"))
    implementation(project(":domain"))
    runtimeOnly(project(":storage"))
    runtimeOnly(project(":external"))
    testImplementation(project(":external"))
    testImplementation(project(":storage"))
}


tasks {
    bootJar {
        enabled = true
    }
    jar {
        enabled = false
    }
}