FROM azul/zulu-openjdk:17-jre-headless

COPY target /opt
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/money-transfer-service-1.0.0-SNAPSHOT-runner.jar"]
