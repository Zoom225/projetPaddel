FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY pom.xml .

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=0 /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

