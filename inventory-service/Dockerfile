FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY shared shared
COPY inventory-service inventory-service
RUN apk add --no-cache maven && \
    mvn -B -pl inventory-service -am -DskipTests package && \
    cp inventory-service/target/*.jar app.jar

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=builder /build/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
