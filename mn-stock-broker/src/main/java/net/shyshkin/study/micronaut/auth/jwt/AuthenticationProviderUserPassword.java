package net.shyshkin.study.micronaut.auth.jwt;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            final Object identity = authenticationRequest.getIdentity();
            final Object secret = authenticationRequest.getSecret();
            log.debug("User {} tries to login...", identity);

            if ("my-user".equals(identity) && "secret".equals(secret)) {
                emitter.onNext(new UserDetails(identity.toString(), List.of()));
                emitter.onComplete();
                return;
            }
            emitter.onError(new AuthenticationException(new AuthenticationFailed("Wrong username or password")));
        }, BackpressureStrategy.ERROR);
    }
}
