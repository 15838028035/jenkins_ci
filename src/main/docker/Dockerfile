FROM adoptopenjdk/openjdk8-openj9:alpine-slim
VOLUME /tmp
ADD jenkinsci-0.0.1-SNAPSHOT.jar /jenkinsci-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/jenkinsci-0.0.1-SNAPSHOT.jar"]
