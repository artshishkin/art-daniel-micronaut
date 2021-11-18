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

[MICRONAUT GRADLE PLUGIN](https://micronaut-projects.github.io/micronaut-gradle-plugin/snapshot/)
-  `gradlew clean dockerBuild` - docker image `FROM openjdk:17-alpine`
    -  size:  339MB
    -  start time: 1737ms, 1115ms, 1330ms
-  `gradlew clean dockerBuildNative` - native
    -  size:  76.8MB
    -  start time: 44ms, 67ms, 50ms

-  `gradlew dockerPush` - first checks if it needs rebuilding
-  `gradlew dockerPushNative` - native - first checks if it needs rebuilding

####  8 Deploy a Micronaut Application as a GraalVM Native Image to AWS Lambda

Follow the Tutorial [DEPLOY A MICRONAUT APPLICATION AS A GRAALVM NATIVE IMAGE TO AWS LAMBDA](https://guides.micronaut.io/latest/mn-application-aws-lambda-graalvm-gradle-java.html)

#####  8.1 Init Application

Launch:
    -  Micronaut Application
    -  Java 11
    -  Gradle
    -  Features
        -  aws-lambda,graalvm

#####  8.2 Build Image

-  `gradlew test`
-   open build/reports/tests/test/index.html
-  `gradlew buildNativeLambda`
-  will generate build/libs/mn-tutorial-graalvm-lambda-0.1-lambda.zip
    -  contains:
    -  bootstrap
    -  func

#####  8.3 Create Lambda Function

-  Name: `mn-tutorial-graalvm-lambda`
-  Runtime: Custom -> Provide your own bootstrap on Amazon Linux 2
-  Create function
-  Upload from
    -  provide zip
-  As Handler, set:
    -  `io.micronaut.function.aws.proxy.MicronautLambdaHandler`   
-  Test it
    -  Event Template: `apigateway-aws-proxy`
    -  Name: `POST_a_Book`
```json
{
  "body": "{\"name\": \"Building Microservices\"}",
  "resource": "/",
  "path": "/",
  "httpMethod": "POST",
  "isBase64Encoded": false,
  "queryStringParameters": {},
  "multiValueQueryStringParameters": {},
  "pathParameters": {},
  "stageVariables": {},
  ...
}
```
-  Results:
    -  Duration: 1.50 ms	
    -  Billed Duration: 2 ms	
    -  Memory Size: 128 MB	
    -  Max Memory Used: 100 MB
-  Encountered Max Duration 600ms

####  11 Tutorial - Deploy a Serverless Micronaut Function to AWS Lambda Java 11 Runtime

Follow the Tutorial [DEPLOY A SERVERLESS MICRONAUT FUNCTION TO AWS LAMBDA JAVA 11 RUNTIME](https://guides.micronaut.io/latest/mn-serverless-function-aws-lambda-maven-java.html)

#####  11.1 Init Application

Launch:
    -  Function Application for Serverless
    -  Java 11
    -  Maven
    -  Features
        -  aws-lambda

#####  11.2 Package

-  `mvnw package`

#####  11.3 Create Lambda Function

-  Name: `mn-tutorial-lambda`
-  Runtime: Java 11 (Corretto)
-  Create function
-  Upload from
    -  provide jar
-  As Handler, set:
    -  `net.shyshkin.study.micronaut.CronJobHandler`
-  Test it
    -  Event Template: `hello-world`
    -  Name: `SendABook`
```json
{
  "name": "Building Microservices"
}
```
-  Results:    
   -  Duration: 1.31 ms	
   -  Billed Duration: 2 ms	
   -  Memory Size: 512 MB	
   -  Max Memory Used: 131 MB	
-  Encountered Max Duration 37.8ms

####  12 Tutorial - Micronaut AWS Lambda and a Cron Job

Follow the Tutorial [MICRONAUT AWS LAMBDA AND A CRON JOB](https://guides.micronaut.io/latest/micronaut-aws-lambda-eventbridge-event-maven-java.html)

#####  12.1 Init Application

Launch:
    -  Function Application for Serverless
    -  Java 11
    -  Maven
    -  Features
        -  aws-lambda

#####  12.2 Application Content

1.  Delete Sample Code
2.  Create Handler



