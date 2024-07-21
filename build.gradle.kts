plugins {
    kotlin("jvm") version "1.9.20"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.1")
}


group = "com.github.mlgpenguin"
version = "2.1.0"
description = "Evaluates mathematical expressions using regex"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        groupId = group as String
        artifactId = "MathEvaluator"
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