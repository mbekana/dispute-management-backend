FROM openjdk:18-alpine

ARG JAR_FILE=target/*.jar
RUN  mkdir /var/storage

#WORKDIR /home/spring
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE develop

ENV PORT 8080
EXPOSE $PORT
CMD [  "java","-jar","app.jar","-Dserver.port=${PORT}","-Duser.timezone=UTC+03:00" , "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}" ]


