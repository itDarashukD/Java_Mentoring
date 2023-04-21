FROM openjdk:11
VOLUME /tmp

LABEL author="Dzmitry_Darashuk"
LABEL description="docker file for business-one service"

ADD ./target/one*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
EXPOSE 8093