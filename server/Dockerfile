FROM gradle:jdk10 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:10-jre-slim
EXPOSE 8080
COPY --from=builder /home/gradle/src/build/distributions/service-poller.tar /app/
WORKDIR /app
RUN tar -xvf service-poller.tar
WORKDIR /app/service-poller
CMD bin/service-poller
