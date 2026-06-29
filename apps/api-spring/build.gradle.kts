plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.sentry.jvm.gradle") version "5.5.0"
}

group = "com.policylab"
version = "0.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:4.1.0"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.sentry:sentry-spring-boot-4-starter:8.46.0")
    implementation("io.sentry:sentry-logback:8.46.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}

sentry {
    includeSourceContext = System.getenv("SENTRY_AUTH_TOKEN")?.isNotBlank() == true
    org = "devpowers"
    projectName = "java-spring-boot"
    authToken = System.getenv("SENTRY_AUTH_TOKEN") ?: ""
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
