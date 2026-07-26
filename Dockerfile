FROM eclipse-temurin:17-jdk

WORKDIR /app

# full project copy
COPY . .

# maven wrapper permission
RUN chmod +x mvnw

# build jar inside docker
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]