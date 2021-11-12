package net.shyshkin.study.micronaut.bootstrap;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.auth.persistence.UserEntity;
import net.shyshkin.study.micronaut.auth.persistence.UserRepository;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class TestDataProvider {

    private final UserRepository repository;

    @EventListener
    void onStartup(StartupEvent startupEvent) {
        if (repository.count() == 0) {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail("art.user@example.com");
            userEntity.setPassword("superSecretPassword");
            UserEntity saved = repository.save(userEntity);
            log.info("User {} saved", saved);
        }
    }

}
