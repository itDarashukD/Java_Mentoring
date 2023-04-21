FROM openjdk:11
VOLUME /tmp

LABEL author="Dzmitry_Darashuk"
LABEL description="docker file for secret service"

ADD ./target/secret*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-Dspring.datasource.url=jdbc:postgresql://db:5432/security","-jar","/app.jar"]
EXPOSE 8082