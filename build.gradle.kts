plugins {
    java
    id("com.gradleup.shadow") version "8.3.6"
}

group = "org.macver.sunny"
version = "v1-1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:5.6.1") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.5.13") // Logging
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0") // JSON serialization
    implementation("org.languagetool:language-en:6.6") // Spelling correction
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        manifest {
            attributes["Main-Class"] = "org.macver.sunny.Sunny"
        }
    }
    build {
        dependsOn(shadowJar)
    }
}

