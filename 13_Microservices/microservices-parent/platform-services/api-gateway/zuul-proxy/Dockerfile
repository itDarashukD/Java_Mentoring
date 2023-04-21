FROM openjdk:11
VOLUME /tmp

LABEL author="Dzmitry_Darashuk"
LABEL description="docker file for zuul-proxy service"

ADD ./target/zuul-proxy*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev,docker","-jar","/app.jar"]
EXPOSE 8091