FROM openjdk:11
VOLUME /tmp

LABEL author="Dzmitry_Darashuk"
LABEL description="docker file for Eureca-client service"

ADD ./target/eureka-client*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
EXPOSE 8089