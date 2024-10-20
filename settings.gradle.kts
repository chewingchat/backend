
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
    id("com.gradle.develocity") version "3.18.1"
}
rootProject.name = "chewing"
include("common")
include("api")
include("domain")
include("storage")
include("external")
