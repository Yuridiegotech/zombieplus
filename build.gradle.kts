plugins {
    id("java")
}

group = "org.zombieplus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.microsoft.playwright:playwright:1.53.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.github.javafaker:javafaker:1.0.2")
    testImplementation("com.google.code.gson:gson:2.11.0")
    testImplementation("org.postgresql:postgresql:42.7.1")
}

tasks.test {
    useJUnitPlatform()
}