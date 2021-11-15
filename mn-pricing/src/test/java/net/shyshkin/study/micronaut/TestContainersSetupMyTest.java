package net.shyshkin.study.micronaut;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class TestContainersSetupMyTest {

    private static final Logger log = LoggerFactory.getLogger(TestContainersSetupMyTest.class);

    // will be shared between test methods
    @Container
    private static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @BeforeAll
    static void beforeAll() {
        log.debug("Bootstrap servers: {}", KAFKA.getBootstrapServers());
    }

    @Test
    void setupWorks() {
        Assertions.assertTrue(KAFKA.isRunning());
    }

}
