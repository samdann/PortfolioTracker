FROM openjdk:8-jdk-alpine
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build

#EXPOSE 8080
#COPY build/libs/*SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]