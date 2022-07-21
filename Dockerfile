FROM gradle:7.5.0-jdk18-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/connect4/
WORKDIR /home/gradle/connect4/
RUN gradle build --no-daemon

FROM openjdk:18-slim
RUN bash -c 'mkdir -pv /opt/connect4/lib/'
COPY --from=build /home/gradle/connect4/app/build/libs/app.jar /opt/connect4/lib/connect4.jar

ENTRYPOINT ["java", "-jar","/opt/connect4/lib/connect4.jar"]
