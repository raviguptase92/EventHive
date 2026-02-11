FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY shared shared
COPY payment-service payment-service
RUN apk add --no-cache maven && \
    mvn -B -pl payment-service -am -DskipTests package && \
    cp payment-service/target/*.jar app.jar

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=builder /build/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
