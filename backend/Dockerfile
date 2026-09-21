# ========================================
# Build Stage
# ========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ========================================
# Runtime Stage
# ========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S mawa3id && adduser -S mawa3id -G mawa3id
USER mawa3id

COPY --from=builder /build/target/*.jar app.jar

ENV TZ=Africa/Casablanca
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Africa/Casablanca", "/app/app.jar"]
