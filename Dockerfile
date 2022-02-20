FROM openjdk:11.0.8-jre-slim

ENV JAR_NAME=spring-boot-security-jwt

WORKDIR /opt

COPY target/$JAR_NAME.jar /opt

#General
ENV JAVA_OPTS="-server"

#Memory
ENV JAVA_OPTS="$JAVA_OPTS -Xmx2g"
ENV JAVA_OPTS="$JAVA_OPTS -Xms1g"

ENTRYPOINT java $JAVA_OPTS -jar $JAR_NAME.jar