# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

COPY pom.xml .
COPY shared shared
COPY order-service order-service

# Build only order-service and its dependencies
RUN apk add --no-cache maven && \
    mvn -B -pl order-service -am -DskipTests package && \
    cp order-service/target/*.jar app.jar

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=builder /build/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
