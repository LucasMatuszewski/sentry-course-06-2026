FROM eclipse-temurin:17-jdk-noble AS build

WORKDIR /workspace
COPY gradle gradle
COPY gradlew gradlew
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies

COPY src src
RUN ./gradlew --no-daemon bootJar

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

USER policylab
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD wget -q -O - http://127.0.0.1:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
