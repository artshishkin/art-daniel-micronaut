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

#####  Install GraalVM SDK on Windows

1.  Download [GraalVM](https://www.graalvm.org)
2.  Unzip
3.  Set Env Variables
    -  `setx /M GRAALVM_HOME "c:\Program Files\Java\graalvm-ce-java11-21.3.0"`
    -  `setx /M PATH "%GRAALVM_HOME%\bin;%PATH%"`
    -  **or** for PowerShell
    -  `$Env:GRAALVM_HOME="c:\Program Files\Java\graalvm-ce-java11-21.3.0"`
    -  `$Env:Path+=";%GRAALVM_HOME%\bin"`
4.  native-image tool installation
    -  `gu install native-image` 

#####  Build Windows native image

1.  Failed tries
    -  `gradlew --no-daemon -Dorg.gradle.java.home="c:\Program Files\Java\graalvm-ce-java11-21.3.0" clean nativeImage`
    -  got errors
        -  `Execution failed for task ':nativeImage'.`
        -  `> Process 'command 'C:\Program Files\Java\graalvm-ce-java11-21.3.0\lib\svm\bin\native-image.exe'' finished with non-zero exit value 1`
    -  **another try**
    -  `gradlew --no-daemon -Dorg.gradle.java.home="c:\Program Files\Java\graalvm-ce-java11-21.3.0" clean nativeCompile`
    -  got errors
        -  `Error: Default native-compiler executable 'cl.exe' not found via environment variable PATH`
        -  `Error: To prevent native-toolchain checking provide command-line option -H:-CheckToolchain`
        -  `Error: Use -H:+ReportExceptionStackTraces to print stacktrace of underlying exception`
        -  `Error: Image build request failed with exit status 1`
    -  cl.exe - install Microsoft Visual Studio
2.  Working solution
    -  in PowerShell run
        -  `cmd.exe /c 'call "c:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Auxiliary\Build\vcvars64.bat" && gradlew --no-daemon -Dorg.gradle.java.home="c:\Program Files\Java\graalvm-ce-java11-21.3.0" clean nativeImage' `
        -  size: application.exe - 52.5MB
3.  Run
    -  `build/native-image/application.exe`
    -  start time:
        -  536ms, 301ms, 465ms, 174ms, 167ms
       


