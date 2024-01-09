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


group = "me.superpenguin"
version = "1.2.3"
description = "Evaluates mathematical expressions using regex"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
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