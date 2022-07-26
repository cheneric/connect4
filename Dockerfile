FROM gradle:7.5.0-jdk18-alpine AS build
ARG PROJECT_NAME=connect4
ARG BUILD_PROJECT_DIR=/home/gradle/${PROJECT_NAME}
COPY --chown=gradle:gradle . ${BUILD_PROJECT_DIR}
WORKDIR ${BUILD_PROJECT_DIR}
RUN gradle build --no-daemon

FROM openjdk:18-slim
ARG PROJECT_NAME=connect4
ARG BUILD_PROJECT_DIR=/home/gradle/${PROJECT_NAME}
ARG BUILD_JAR=${BUILD_PROJECT_DIR}/app/build/libs/app.jar
ARG RUN_JAR_DIR=/opt/${PROJECT_NAME}/lib
ARG RUN_JAR=${RUN_JAR_DIR}/${PROJECT_NAME}.jar
ENV RUN_JAR_DIR ${RUN_JAR_DIR}
RUN mkdir -pv ${RUN_JAR_DIR}
COPY --from=build ${BUILD_JAR} ${RUN_JAR}

ENV RUN_JAR ${RUN_JAR}
ENTRYPOINT java -jar ${RUN_JAR}
