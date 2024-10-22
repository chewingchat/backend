dependencies {
    implementation(project(":common"))
    compileOnly(project(":domain"))
    compileOnly("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")
    implementation("com.google.firebase:firebase-admin:6.8.1")
    implementation("com.squareup.okhttp3:okhttp:4.2.2")
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
