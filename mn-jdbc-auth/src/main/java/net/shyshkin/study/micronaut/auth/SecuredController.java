package net.shyshkin.study.micronaut.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/secured")
public class SecuredController {

    @Inject
    ObjectMapper objectMapper;

    @Get(value = "/status", produces = MediaType.APPLICATION_JSON)
    public Map<String, List<String>> status(Principal principal) throws JsonProcessingException {
        Authentication details = (Authentication) principal;
        log.debug("User Details: {}", objectMapper.writeValueAsString(details));
        var hairColor = (String) details.getAttributes().get("hair_color");
        var language = (String) details.getAttributes().get("language");
        return Map.of("user", List.of(principal.getName(), hairColor, language));
    }

}
