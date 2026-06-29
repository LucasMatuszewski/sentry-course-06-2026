plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.sentry.jvm.gradle") version "5.5.0"
}

group = "com.policylab"
// Version is overridden at build time via -Pversion=api-spring@<sha> in
// .github/workflows/release.yml. The Sentry Gradle plugin uses this as
// the release identifier when uploading the source bundle, and Spring
// Boot writes it into BuildProperties (build-info.properties) which the
// Sentry Spring Boot starter reads at runtime to set the release tag on
// every event. One value, derived from git, used in three places.
version = (project.findProperty("releaseName") as String?) ?: "api-spring@dev"

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
    // Pinned to 8.11.1 to match the bundled sentry-core that the
    // io.sentry.jvm.gradle:5.5.0 plugin injects on the classpath. See
    // TDD.md for the version-mismatch crash this avoids.
    //
    // Trade-off accepted for workshop stability: the Sentry.logger()
    // Logs API (>=8.15.1) and sentry-async-profiler (>=8.23.0) are not
    // available at this SDK line. Tracing, breadcrumbs, custom spans,
    // and Insights all still work. The slide accordion documents this.
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:8.11.1")
    implementation("io.sentry:sentry-logback:8.11.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

sentry {
    includeSourceContext = System.getenv("SENTRY_AUTH_TOKEN")?.isNotBlank() == true
    org = "devpowers"
    projectName = "java-spring-boot"
    authToken = System.getenv("SENTRY_AUTH_TOKEN") ?: ""
}

// Spring Boot 3.x writes BuildProperties to META-INF/build-info.properties
// only when the bootBuildInfo task runs. The Sentry Spring Boot starter
// reads BuildProperties.getVersion() and uses it as the default release
// when sentry.release is not explicitly set.
springBoot {
    buildInfo()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
