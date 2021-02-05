FROM adoptopenjdk/openjdk8-openj9:alpine-slim
VOLUME /tmp
RUN mkdir app
RUN mkdir -p /app/config

ADD jenkinsci-0.0.1-SNAPSHOT.jar /app/jenkinsci-0.0.1-SNAPSHOT.jar
COPY config/* /app/config/

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/jenkinsci-0.0.1-SNAPSHOT.jar"]
