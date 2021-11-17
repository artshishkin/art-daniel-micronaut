# art-daniel-micronaut
Learn Micronaut - cloud native microservices with Java - Tutorial from Daniel Prinz and Franz Licht (Udemy)

####  Section 2: Micronaut 3 - Quickstart

#####  5. Your first Micronaut Application

-  [Launch micronaut.io](https://micronaut.io/launch/)

#####  8. Setup IDE

For more advanced Micronaut Features
-  Enable Annotation Processing

####  Section 4: Micronaut Web

#####  35. Open API and Swagger

-  `http://localhost:8080/swagger/mn-stock-broker-0.0.yml` - specification
-  `http://localhost:8080/swagger-ui` - specification
-  `http://localhost:8080/redoc` - redoc
-  `http://localhost:8080/rapidoc` - rapidoc

####  Section 8: Using Web Sockets

#####  64. Web Socket Server

WebSocket in browser
-  extension for Chrome - Smart Websocket Client
-  **or** 
-  [https://websocketking.com/](https://websocketking.com/)

####  Section 9: GraalVM - Native Image

#####  67.5 Docker Image Build Commands

-  `./gradlew clean assemble` - rebuild the project and package JAR
-  `docker build . -t artarkatesoft/mn-pricing`

#####  67.6 Docker Stack Deploy (by Daniel)

As Kafka runs on the host machine the stack must be attached to the correct host network, so the access to kafka works.
-  `docker stack deploy -c app.stack.yml mn-pricing-stack`
-  will not work - because my docker is not in Swarm mode

####  10 Tutorial - Creating your first micronaut graal application

Follow the tutorial [CREATING YOUR FIRST MICRONAUT GRAAL APPLICATION](https://guides.micronaut.io/latest/micronaut-creating-first-graal-app-gradle-java.html)

Build docker native image:
-  `gradlew clean dockerBuildNative`
Run container
-  `docker run -p 8080:8080 mn-tutorial-first-graalvm`
Tag container to push to DockerHub
-  `docker tag mn-tutorial-first-graalvm artarkatesoft/mn-tutorial-first-graalvm`
Push to Docker Hub
-  `docker push artarkatesoft/mn-tutorial-first-graalvm`







