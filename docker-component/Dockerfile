FROM openjdk:8-jre-alpine
ADD scripts-morph-v11.5.jar  /scripts-morph-v11.5.jar
ADD resources /resources
ENTRYPOINT ["/usr/bin/java", "-jar", "-Dspring.profiles.active=test", "scripts-morph-v11.5.jar"]
