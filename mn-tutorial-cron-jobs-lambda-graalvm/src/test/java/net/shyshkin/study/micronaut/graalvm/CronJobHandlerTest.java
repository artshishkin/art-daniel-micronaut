package net.shyshkin.study.micronaut.graalvm;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

public class CronJobHandlerTest {

    private static CronJobHandler cronJobHandler;

    @BeforeAll
    public static void setupServer() {
        cronJobHandler = new CronJobHandler();
    }

    @AfterAll
    public static void stopServer() {
        if (cronJobHandler != null) {
            cronJobHandler.getApplicationContext().close();
        }
    }

    @Test
    public void testHandler() {
        Map<String, Object> event = Map.of(
                "version", 0,
                "id", UUID.randomUUID(),
                "detail-type", "Scheduled Event",
                "source", "aws.events",
                "account", 1234567899,
                "time", "2021-05-19T09:03:02Z",
                "region", "us-east-1",
                "resources", "[arn:aws:events:us-east-1:1234567899:rule/5minutes]",
                "detail", "{}"
        );
        cronJobHandler.execute(event);
    }
}
