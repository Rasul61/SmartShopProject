FROM alpine:latest
RUN apk add --no-cache openjdk21
COPY build/libs/CarRentalSystem-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/
RUN mv /app/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]