FROM openjdk:11
VOLUME /tmp

LABEL author="Dzmitry_Darashuk"
LABEL description="docker file for gateway-second-client-sevice service"

ADD ./target/secondClientService*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev,docker","-jar","/app.jar"]
EXPOSE 8085