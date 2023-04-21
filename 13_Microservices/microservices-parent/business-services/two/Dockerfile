FROM openjdk:11
VOLUME /tmp

LABEL author="Dzmitry_Darashuk"
LABEL description="docker file for business-two service"

ADD ./target/two*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8095