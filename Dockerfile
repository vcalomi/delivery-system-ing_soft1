FROM maven:3.9.9-amazoncorretto-23
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -Dmaven.test.skip=true
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/tp-0.0.1-SNAPSHOT.jar"]