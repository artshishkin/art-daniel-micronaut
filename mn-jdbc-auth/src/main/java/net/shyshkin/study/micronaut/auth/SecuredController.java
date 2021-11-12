package net.shyshkin.study.micronaut.auth;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/secured")
public class SecuredController {

    @Get(value = "/status", produces = MediaType.TEXT_PLAIN)
    public String status(Principal principal) {
        return "Name: " + principal.getName();
    }

}
