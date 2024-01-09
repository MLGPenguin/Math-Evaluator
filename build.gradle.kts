plugins {
    kotlin("jvm") version "1.7.21"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.1")
}


group = "com.github.mlgpenguin"
version = "1.2.5"
description = "Evaluates mathematical expressions using regex"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        artifactId = "math-evaluator"
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

kotlin {
    jvmToolchain(17)
}