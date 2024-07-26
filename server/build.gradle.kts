plugins {
    kotlin("jvm") version "1.9.23"
    id("org.springframework.boot") version "2.7.3"
}

group = "com.dn.todo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.3")
}

kotlin {
    jvmToolchain(17)
}