FROM maven:3.9.9-amazoncorretto-23
WORKDIR /app
# Render inyecta PORT en runtime; si no existe, Spring usa 8081 (server.port=${PORT:8081})
ENV PORT=8081
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "target/tp-0.0.1-SNAPSHOT.jar"]