plugins {
    java
    // Spring Boot 3.5 instead of 4.x: sentry-spring-boot-4-starter 8.46.0
    // is internally the SB3-jakarta auto-config and references the
    // org.springframework.boot.web.client.RestClientCustomizer class
    // which was removed in Spring Boot 4.1. Sentry's real Spring Boot 4
    // starter is not stable in 8.46.0. The Sentry-side teaching points
    // are identical between SB3.5 and SB4.
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.6"
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // sentry-spring-boot-starter-jakarta is the SB3 (jakarta.*) Sentry
    // integration. It auto-wires the exception resolver, OpenTelemetry
    // tracing, breadcrumbs, and the sentry.* property binding.
    // Maven coordinates: io.sentry:sentry-spring-boot-starter-jakarta
    // (suffix -jakarta on the artifact, NOT infix -jakarta-starter).
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:8.46.0")
    implementation("io.sentry:sentry-logback:8.46.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
