FROM eclipse-temurin:17-jdk-noble AS build

WORKDIR /workspace
COPY gradle gradle
COPY gradlew gradlew
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies

# Release identifier flows from the GitHub Actions release.yml workflow
# (SENTRY_RELEASE=api-spring@<short-sha>) into the build phase via
# --build-arg SENTRY_RELEASE=... Then -PreleaseName= passes it to Gradle,
# which sets project.version. The Sentry Gradle plugin uses that same
# project.version as the release tag when uploading the source bundle.
# Default keeps local docker builds working without a sha.
ARG SENTRY_RELEASE=api-spring@dev
ENV SENTRY_RELEASE=${SENTRY_RELEASE}

COPY src src
RUN ./gradlew --no-daemon bootJar -PreleaseName=${SENTRY_RELEASE}

FROM eclipse-temurin:17-jre-noble

# Ubuntu (noble) base instead of Alpine because eclipse-temurin's Alpine
# variants do not publish a linux/arm64 manifest. Coolify on the Hetzner
# Ampere host needs arm64. Image size delta is ~70 MB — acceptable.
RUN apt-get update \
 && apt-get install -y --no-install-recommends wget \
 && rm -rf /var/lib/apt/lists/* \
 && groupadd --system policylab \
 && useradd  --system --gid policylab policylab
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar

# Carry the release identifier into the runtime image so the Spring
# Sentry starter reads it from the OS env at startup. application.properties
# already has sentry.release=${SENTRY_RELEASE:} which resolves this.
ARG SENTRY_RELEASE=api-spring@dev
ENV SENTRY_RELEASE=${SENTRY_RELEASE}

USER policylab
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD wget -q -O - http://127.0.0.1:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
