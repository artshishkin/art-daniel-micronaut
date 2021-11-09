package net.shyshkin.study.micronaut.hello;

import jakarta.inject.Singleton;

@Singleton
public class HelloWorldService {

    public String helloFromService(){
        return "Hello from Service";
    }

}
