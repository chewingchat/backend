dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(project(":common"))
    implementation(project(":domain"))
    runtimeOnly(project(":storage"))
    runtimeOnly(project(":external"))
    testImplementation(project(":external"))
    testImplementation(project(":storage"))
    testImplementation(project(":tests:api-docs"))
}

tasks {
    bootJar {
        enabled = true
    }
    jar {
        enabled = false
    }
}

val snippetsDir by extra { file("build/generated-snippets") }

tasks {
    val resultDir = file("src/main/resources/static/docs")

    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    asciidoctor {
        dependsOn(test) // 3
        baseDirFollowsSourceDir() // 4
        doLast {
            copy {
                from(outputDir)
                into(resultDir)
            }
        } // 5
    }

    val copyDocs by registering(Copy::class) {
        dependsOn(asciidoctor)
        from(asciidoctor.get().outputDir)
        into(file(resultDir))
    }

    jar {
        enabled = false
    }

    bootJar {
        enabled = true
        dependsOn(copyDocs)
    }
}
