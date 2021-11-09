package net.shyshkin.study.micronaut.hello;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/hello")
public class HelloWorldController {

    public static final Logger LOG = LoggerFactory.getLogger(HelloWorldController.class);

    private final String helloFromConfig;

    private final MyService service;

    public HelloWorldController(
            @Property(name = "hello.world.message") String helloFromConfig,
            MyService service) {
        this.helloFromConfig = helloFromConfig;
        this.service = service;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloWorld() {
        LOG.debug("Called the hello World API");
        return this.service.helloFromService();
    }

    @Get(uri = "/config", produces = MediaType.TEXT_PLAIN)
    public String config() {
        LOG.debug("Called the config API");
        return helloFromConfig;
    }

}
