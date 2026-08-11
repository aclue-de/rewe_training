# Build stage. Maven comes from the image, so the wrapper stays a local concern.
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# Dependencies first: this layer survives every change that leaves pom.xml alone.
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp package -DskipTests

# Runtime stage. A JRE is enough, no build tools in the final image.
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

RUN useradd --system --uid 1001 spring
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
