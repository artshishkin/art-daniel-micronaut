package net.shyshkin.study.micronaut;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class TestContainersSetup {

    private static final Logger log = LoggerFactory.getLogger(TestContainersSetup.class);

    @Rule
    public KafkaContainer kafka = new KafkaContainer();

    @Test
    void setupWorks() {
        kafka.start();
        log.debug("Bootstrap servers: {}", kafka.getBootstrapServers());
        kafka.stop();
    }

}
