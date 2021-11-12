package net.shyshkin.study.micronaut.auth;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.auth.persistence.UserRepository;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class JdbcAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository repository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String identity = (String) authenticationRequest.getIdentity();
        Object secret = authenticationRequest.getSecret();
        log.debug("User {} tries to login ...", identity);

        return Mono.justOrEmpty(repository.findByEmail(identity))
                .filter(user -> Objects.equals(user.getPassword(), secret))
                .map(user -> AuthenticationResponse.success(user.getEmail()))
                .switchIfEmpty(Mono.just(AuthenticationResponse.failure("Wrong username or password")));
    }
}
