package net.shyshkin.study.micronaut.auth;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.auth.persistence.UserEntity;
import net.shyshkin.study.micronaut.auth.persistence.UserRepository;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        return Flowable.create(emitter -> {

            Optional<UserEntity> maybeUser = repository.findByEmail(identity);
            if (maybeUser.isPresent()) {
                UserEntity user = maybeUser.get();
                log.debug("Found user: {}", user.getEmail());
                if (user.getPassword().equals(secret)) {
                    log.debug("User logged in.");
                    Map<String, Object> attributes = Map.of(
                            "hair_color", "brown",
                            "language", "en"
                    );
                    emitter.onNext(
                            AuthenticationResponse.success(
                                    user.getEmail(),
                                    List.of("ROLE_USER"),
                                    attributes)
                    );
                    emitter.onComplete();
                    return;
                } else {
                    log.debug("Wrong password provided for user: {}", identity);
                }
            } else {
                log.debug("No user found with email: {}", identity);
            }
            emitter.onError(new AuthenticationException(new AuthenticationFailed("Wrong username or password")));
        }, BackpressureStrategy.ERROR);
    }
}
