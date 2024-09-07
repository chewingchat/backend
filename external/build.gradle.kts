dependencies {
    implementation(project(":common"))
    compileOnly(project(":domain"))
    compileOnly("org.springframework:spring-context")
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
