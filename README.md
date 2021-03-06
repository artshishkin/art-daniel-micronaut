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
    -  `net.shyshkin.study.micronaut.BookRequestHandler`
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

#####  12.3 Logger Configuration

#####  12.4 Create Lambda Function

-  Name: `mn-tutorial-cron-jobs-lambda`
-  Runtime: Java 11 (Corretto)
-  Create function
-  Upload from
    -  provide jar
-  As Handler, set:
    -  `net.shyshkin.study.micronaut.CronJobHandler`
-  Test it
    -  New Event - CloudWatch
        -  Scheduled
        -  Name: 5minutesEvent
    -  Save changes
    -  Test
```json
{
  "id": "cdc73f9d-aea9-11e3-9d5a-835b769c0d9c",
  "detail-type": "Scheduled Event",
  "source": "aws.events",
  "account": "123456789012",
  "time": "1970-01-01T00:00:00Z",
  "region": "us-east-1",
  "resources": [
    "arn:aws:events:us-east-1:123456789012:rule/ExampleRule"
  ],
  "detail": {}
}
```    
-  Add trigger
    -  Event Bridge (CloudWatchEvents)
    -  Create a new Rule
        -  Rule name: `5minutes`
        -  Rule description: `Scheduled expression every 5 minutes`
        -  Schedule expression: `rate(5 minutes)`
-  View Cloud Watch Logs
```
2021-11-18 09:19:17 6b38091d-97c2-4c08-8345-5f9944047faa mn-tutorial-cron-jobs-lambda $LATEST arn:aws:lambda:eu-north-1:392971033516:function:mn-tutorial-cron-jobs-lambda 512  14995 1-61961a93-5a2ea5c867a6f13e6b88bbd5 TRACE n.s.s.m.CronJobHandler - {version=0, id=2bc60a77-2633-05e1-026f-ee2ac182f918, detail-type=Scheduled Event, source=aws.events, account=392971033516, time=2021-11-18T09:19:12Z, region=eu-north-1, resources=[arn:aws:events:eu-north-1:392971033516:rule/5minutes], detail={}}
```
-  Results:
    -  Duration: 1.17 ms	
    -  Billed Duration: 2 ms	
    -  Memory Size: 512 MB	
    -  Max Memory Used: 127 MB	
-  Encountered Max Duration 6.24ms

#####  12.5 Compare with native

1. Launch:
    -  Function Application for Serverless
    -  Java 11
    -  Gradle
    -  Features
        -  aws-lambda,graalvm
2.  Modify App content and Tests
3.  Modify logback configuration
4.  Build native lambda
    -  `gradlew clean buildNativeLambda`        
5.  Create Lambda function        
    -  Name: `mn-tutorial-cron-jobs-lambda-graalvm`
    -  Runtime: Custom -> Provide your own bootstrap on Amazon Linux 2
    -  Create function
    -  Upload from
        -  provide zip
    -  As Handler, set:
        -  `net.shyshkin.study.micronaut.graalvm.CronJobHandler`   
6.  Test it
    -  New Event - CloudWatch
        -  Scheduled
        -  Name: 5minutesEvent
    -  Save changes
    -  Test
```json
{
  "id": "cdc73f9d-aea9-11e3-9d5a-835b769c0d9c",
  "detail-type": "Scheduled Event",
  "source": "aws.events",
  "account": "123456789012",
  "time": "1970-01-01T00:00:00Z",
  "region": "us-east-1",
  "resources": [
    "arn:aws:events:us-east-1:123456789012:rule/ExampleRule"
  ],
  "detail": {}
}
``` 
-  Got an Error
```
by: io.micronaut.http.codec.CodecException: Error decoding stream for type [class com.amazonaws.services.lambda.runtime.events.ScheduledEvent]: Joda date/time type `org.joda.time.DateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-joda" to enable handling
at [Source: (byte[])"{"id":"cdc73f9d-aea9-11e3-9d5a-835b769c0d9c","detail-type":"Scheduled Event","source":"aws.events","account":"123456789012","time":"1970-01-01T00:00:00Z","region":"us-east-1","resources":["arn:aws:events:us-east-1:123456789012:rule/ExampleRule"],"detail":{}}"; line: 1, column: 132] (through reference chain: com.amazonaws.services.lambda.runtime.events.ScheduledEvent["time"])
```

#####  12.6 Fixing Error with JodaTime

-  added `com.fasterxml.jackson.datatype:jackson-datatype-joda` dependency
-  got an Error
```
Caused by: com.oracle.graal.pointsto.constraints.UnsupportedFeatureException: No instances of org.joda.time.UTCDateTimeZone are allowed in the image heap as this class should be initia
lized at image runtime. To see how this object got instantiated use --trace-object-instantiation=org.joda.time.UTCDateTimeZone.

```

####  13 Tutorial - Micronaut AWS Lambda and S3 Event

Follow the tutorial [MICRONAUT AWS LAMBDA AND S3 EVENT](https://guides.micronaut.io/latest/micronaut-aws-lambda-s3-event-maven-java.html)

#####  13.1 Init Application

Launch:
    -  Function Application for Serverless
    -  Java 11
    -  Maven
    -  Features
        -  aws-lambda

#####  13.6 Create S3 Bucket

Go to AWS S3 and create bucket
-  Name: `mn-tutorial-s3-lambda`
-  Create folders:
    -  images
    -  thumbnails

#####  13.7 Create Lambda Function

-  Name: `mn-tutorial-s3-lambda`
-  Runtime: Java 11 (Corretto)
-  Role: Create a new role with basic Lambda permissions
    -  Lambda will create an execution role named mn-tutorial-s3-lambda-role-<random>, with permission to upload logs to Amazon CloudWatch Logs.
    -  We will modify it later to access S3 (Get/Put Object)    
-  Create function
-  Upload from
    -  provide jar (`mvnw clean package` before)
-  As Handler, set:
    -  `net.shyshkin.study.micronaut.thumbnail.ThumbnailHandler`   

#####  13.8 Modify IAM Role of Lambda

Create Policy
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowReadFromImages",
            "Effect": "Allow",
            "Action": [
                "s3:GetObject"
            ],
            "Resource": [
                "arn:aws:s3:::mn-tutorial-s3-lambda/images"
            ]
        },
        {
            "Sid": "AllowPutToThumbnails",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject"
            ],
            "Resource": [
                "arn:aws:s3:::mn-tutorial-s3-lambda/thumbnails"
            ]
        }
    ]
}
```
Review Policy
    -  name: `mn-tutorial-s3-lambda-policy`
    -  Description: `Policy to Allow lambda Function to Read From S3 images folder and write to thumbnails folder`

IAM Role
    -  `mn-tutorial-s3-lambda-role-haku8may`
    -  Attach Policy `mn-tutorial-s3-lambda-policy`

#####  13.9 Add triggers

-  Create 2 triggers: 1 for `jpg`, another - for `png`
-  Trigger configuration
    -  S3
    -  Bucket: `mn-tutorial-s3-lambda`
    -  Event type: PUT
    -  Prefix: `images/`
    -  Suffix: `.jpg` 
-  Test it
    -  Upload jpg or png file into `images/`
    -  View thumbnails in `thumbnails/`
    -  Monitor -> execution time
    -  CloudWatch logs 

#####  13.10 Compare with native

1.  Build native image
    -  `gradlew clean buildNativeLambda`
2.  Create Lambda Function
    -  Name: `mn-tutorial-s3-lambda-graalvm`    
    -  Runtime: Custom -> Provide your own bootstrap on Amazon Linux 2
    -  Role: Create a new Role with basic Lambda permissions
    -  Create function
    -  Upload from
        -  provide zip
    -  As Handler, set:
        -  `net.shyshkin.study.micronaut.thumbnail.graalvm.ThumbnailHandler`   
    -  Modify IAM Role
        -  Attach policy `mn-tutorial-s3-lambda-policy`
3.  Test it
    -  Got an Error
```
Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
at [Source: (byte[])"{"Records":[{"eventVersion":"2.0","eventSource":"aws:s3","awsRegion":"us-east-1","eventTime":"1970-01-01T00:00:00.000Z","eventName":"ObjectCreated:Put","userIdentity":{"principalId":"EXAMPLE"},"requestParameters":{"sourceIPAddress":"127.0.0.1"},"responseElements":{"x-amz-request-id":"EXAMPLE123456789","x-amz-id-2":"EXAMPLE123/5678abcdefghijklambdaisawesome/mnopqrstuvwxyzABCDEFGH"},"s3":{"s3SchemaVersion":"1.0","configurationId":"testConfigRule","bucket":{"name":"example-bucket","ownerIdentity":{"[truncated 182 bytes]; line: 1, column: 2]
```

#####  13.11  Custom S3EventNotification with Introspected annotation

#####  13.12  S3 Event Notification Introspection

#####  13.13  Trying to read data using s3Client.getObject

```
Invocation with requestId [d5578650-5be7-434d-8751-a7e92ec7ca5c] failed: sun.java2d.Disposer.addRecord(Ljava/lang/Object;JJ)Vjava.lang.NoSuchMethodError: sun.java2d.Disposer.addRecord(Ljava/lang/Object;JJ)V
at com.oracle.svm.jni.functions.JNIFunctions$Support.getMethodID(JNIFunctions.java:1114)
at com.oracle.svm.jni.functions.JNIFunctions$Support.getMethodID(JNIFunctions.java:1099)
at com.oracle.svm.jni.functions.JNIFunctions.GetStaticMethodID(JNIFunctions.java:416)
at sun.java2d.Disposer.initIDs(Disposer.java)
at sun.java2d.Disposer.<clinit>(Disposer.java:71)
at javax.imageio.stream.FileCacheImageInputStream.<init>(FileCacheImageInputStream.java:114)
at com.sun.imageio.spi.InputStreamImageInputStreamSpi.createInputStreamInstance(InputStreamImageInputStreamSpi.java:69)
at javax.imageio.ImageIO.createImageInputStream(ImageIO.java:357)
at net.coobird.thumbnailator.tasks.io.InputStreamImageSource.read(Unknown Source)
at net.coobird.thumbnailator.tasks.SourceSinkThumbnailTask.read(Unknown Source)
at net.coobird.thumbnailator.Thumbnailator.createThumbnail(Unknown Source)
at net.coobird.thumbnailator.Thumbnails$Builder.toOutputStream(Unknown Source)
at net.shyshkin.study.micronaut.thumbnail.graalvm.generator.ThumbnailatorThumbnailGenerator.thumbnail(ThumbnailatorThumbnailGenerator.java:34)
at net.shyshkin.study.micronaut.thumbnail.graalvm.generator.$ThumbnailatorThumbnailGenerator$Definition$Intercepted.$$access$$thumbnail(Unknown Source)
at net.shyshkin.study.micronaut.thumbnail.graalvm.generator.$ThumbnailatorThumbnailGenerator$Definition$Exec.dispatch(Unknown Source)
at io.micronaut.aop.chain.MethodInterceptorChain.proceed(MethodInterceptorChain.java:128)
at io.micronaut.validation.ValidatingInterceptor.intercept(ValidatingInterceptor.java:138)
at io.micronaut.aop.chain.MethodInterceptorChain.proceed(MethodInterceptorChain.java:137)
at net.shyshkin.study.micronaut.thumbnail.graalvm.generator.$ThumbnailatorThumbnailGenerator$Definition$Intercepted.thumbnail(Unknown Source)
at net.shyshkin.study.micronaut.thumbnail.graalvm.ThumbnailHandler.lambda$persistThumbnail$3(ThumbnailHandler.java:80)
at java.util.Optional.flatMap(Optional.java:294)
at net.shyshkin.study.micronaut.thumbnail.graalvm.ThumbnailHandler.persistThumbnail(ThumbnailHandler.java:80)
at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:177)
at java.util.stream.ReferencePipeline$11$1.accept(ReferencePipeline.java:442)
at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1655)
at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:658)
at java.util.stream.ReferencePipeline$7$1.accept(ReferencePipeline.java:274)
at java.util.stream.Streams$StreamBuilderImpl.forEachRemaining(Streams.java:411)
at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
at net.shyshkin.study.micronaut.thumbnail.graalvm.ThumbnailHandler.execute(ThumbnailHandler.java:70)
at net.shyshkin.study.micronaut.thumbnail.graalvm.ThumbnailHandler.execute(ThumbnailHandler.java:22)
at io.micronaut.function.aws.MicronautRequestHandler.handleRequest(MicronautRequestHandler.java:90)
at io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime.startRuntimeApiEventLoop(AbstractMicronautLambdaRuntime.java:347)
at io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime.run(AbstractMicronautLambdaRuntime.java:129)
at net.shyshkin.study.micronaut.ThumbnailLambdaRuntime.main(ThumbnailLambdaRuntime.java:16)
```

####  14 Tutorial - Deploy a Micronaut Application to AWS Elastic Beanstalk

Follow the Tutorial [DEPLOY A MICRONAUT APPLICATION TO AWS ELASTIC BEANSTALK](https://guides.micronaut.io/latest/micronaut-elasticbeanstalk-maven-java.html)

#####  14.1 Init Application

Launch:
    -  Micronaut Application
    -  Java 11
    -  Maven
    -  Features:
        -  lombok
    
#####  14.3 Deploy to Elastic Beanstalk    
    
1.  Package App
    -  `mvnw clean package`
2.  Create Beanstalk Application
    -  Platform: Java 11
    -  Upload your code -> jar
    -  Version label: `mn-tutorial-beanstalk-source-0.1`
    -  Configure more options
3.  Configure Environment
    -  Presents: single instance
    -  Environment Properties
        -  MICRONAUT_SERVER_PORT: 5000
        -  Save
    -  Create app
4.  Test it
    -  Environment
    -  Go to environment
    -  Add `/health`
    -  like this `http://mntutorialbeanstalk-env.eba-eu6xicim.eu-north-1.elasticbeanstalk.com/health`
    -  `{"status":"UP"}`
5.  Clean up
    -  application
    -  Delete                    

####  15 Tutorial - Download an Excel file in a Micronaut Application

Follow the Tutorial [DOWNLOAD AN EXCEL FILE IN A MICRONAUT APPLICATION](https://guides.micronaut.io/latest/micronaut-file-download-excel-maven-java.html)

#####  15.1 Init Application

Launch:
    -  Micronaut Application
    -  Java 11
    -  Maven
    -  Features:
        -  lombok




