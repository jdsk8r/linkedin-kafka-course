plugins {
    kotlin("jvm") version "2.4.20"
    application
}

group = "no.sanchezrolfsen.learning"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.kafka:kafka-clients:4.3.1")
    implementation("org.slf4j:slf4j-api:2.0.19")
    implementation("org.slf4j:slf4j-simple:2.0.19")
}

kotlin {
    jvmToolchain(26)
}

application {
    mainClass = "ProducerDemoKt"
}

tasks.test {
    useJUnitPlatform()
}