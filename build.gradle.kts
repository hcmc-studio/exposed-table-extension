plugins {
    kotlin("jvm") version "1.9.0"
    id("maven-publish")
}

group = "studio.hcmc"
version = "0.0.4"

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "studio.hcmc"
            artifactId = "exposed-table-extension"
            version = "0.0.4"
            from(components["java"])
        }
    }
}

dependencies {
    implementation("com.github.hcmc-studio:exposed-transaction-extension:0.0.4-release")
    implementation("com.github.hcmc-studio:kotlin-format-extension:0.0.4-release")

    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.41.1")
}