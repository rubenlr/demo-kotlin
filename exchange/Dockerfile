# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:21-alpine-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the application's JAR file to the container
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]