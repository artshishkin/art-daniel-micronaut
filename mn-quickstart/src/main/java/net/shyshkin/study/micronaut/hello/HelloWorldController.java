package net.shyshkin.study.micronaut.hello;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/hello")
public class HelloWorldController {

    private final String helloFromConfig;

    private final MyService service;

    private final HelloWorldTranslationConfig translationConfig;

    public HelloWorldController(
            @Property(name = "hello.world.message") String helloFromConfig,
            HelloWorldTranslationConfig translationConfig,
            MyService service) {
        this.helloFromConfig = helloFromConfig;
        this.service = service;
        this.translationConfig = translationConfig;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloWorld() {
        log.debug("Called the hello World API");
        return this.service.helloFromService();
    }

    @Get(uri = "/config", produces = MediaType.TEXT_PLAIN)
    public String config() {
        log.debug("Called the config API");
        return helloFromConfig;
    }

    @Get(uri = "/translation", produces = MediaType.APPLICATION_JSON)
    public HelloWorldTranslationConfig translation() {
        log.debug("Called the translation API");
        return translationConfig;
    }

    @Get("/json")
    public Greeting json() {
        return new Greeting();
    }

}
