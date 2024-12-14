FROM openjdk:17-jdk-slim

WORKDIR /app
COPY . .

RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

ARG JAR_FILE=build/libs/Task_Management_System-0.0.1-SNAPSHOT.jar

RUN cp ${JAR_FILE} app.jar

CMD ["java", "-jar", "app.jar"]
