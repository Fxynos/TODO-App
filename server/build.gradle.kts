import java.util.*

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.springframework.boot") version "2.7.3"
    id("org.liquibase.gradle") version "2.2.0"
}

group = "com.dn.todo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.3")
    runtimeOnly("com.h2database:h2:2.3.230")

    liquibaseRuntime("com.h2database:h2:2.3.230")
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
}

kotlin {
    jvmToolchain(17)
}

tasks.bootJar {
    archiveFileName = "server.jar"
}

val propertiesPath = "../dev.properties"
val fileProperties = Properties().apply { file(propertiesPath).inputStream().use { load(it) } }

liquibase {
    activities {
        register("main") {
            arguments = mapOf(
                "changelogFile" to "changelog.sql",
                "url" to (
                    properties["db.h2.address"] ?: // explicitly defined properties in terminal
                    fileProperties["db.h2.address"] ?:
                    throw IllegalArgumentException("define db.h2.address property")
                )
            )
        }
    }
}

tasks.processResources {
    from(propertiesPath).rename {
        "application.properties"
    }
}