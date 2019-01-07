FROM java:8
FROM maven:alpine

WORKDIR /app
EXPOSE 8080
LABEL maintainer=“email.shanu@gmail.com”
COPY target/subscription-pricing-0.0.1-SNAPSHOT.jar /app/subscription-pricing-0.0.1-SNAPSHOT.jar
COPY . /app
ENTRYPOINT ["java","-jar","subscription-pricing-0.0.1-SNAPSHOT.jar"]