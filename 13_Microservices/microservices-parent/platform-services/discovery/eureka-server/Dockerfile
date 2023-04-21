FROM openjdk:11
VOLUME /tmp

LABEL author="Dzmitry_Darashuk"
LABEL description="docker file for Eureca-server service"

ADD ./target/eureka-server*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8761