plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "sag.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-kafka")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}