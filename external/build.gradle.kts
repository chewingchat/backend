dependencies {
    implementation(project(":common"))
    compileOnly(project(":domain"))
    compileOnly("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")
    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-client")
}
