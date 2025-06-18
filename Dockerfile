FROM openjdk:21
COPY ./build/libs/Sunny-v*.jar /tmp/Sunny.jar
WORKDIR /tmp
ENTRYPOINT ["java","-jar","Sunny.jar"]