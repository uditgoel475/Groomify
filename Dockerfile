# syntax=docker/dockerfile:1.6

# ---- build ----
FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /workspace

# Resolve dependencies first for cache friendliness.
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -DskipTests package

# Spring Boot layered jar — split deps / loader / app for better Docker layer reuse.
RUN java -Djarmode=layertools -jar target/groomify-0.0.1-SNAPSHOT.jar extract --destination /extracted

# ---- runtime ----
FROM eclipse-temurin:17-jre
RUN apt-get update \
  && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/* \
  && groupadd -r app && useradd -r -g app app
WORKDIR /app

COPY --from=build --chown=app:app /extracted/dependencies/ ./
COPY --from=build --chown=app:app /extracted/spring-boot-loader/ ./
COPY --from=build --chown=app:app /extracted/snapshot-dependencies/ ./
COPY --from=build --chown=app:app /extracted/application/ ./

USER app:app
EXPOSE 8080

# Memory: -XX:MaxRAMPercentage works inside containers with cgroup limits.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher \"$@\"","--"]
