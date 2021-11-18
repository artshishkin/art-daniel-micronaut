package net.shyshkin.study.micronaut.graalvm;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Introspected
public class CronJobHandler extends MicronautRequestHandler<ScheduledEvent, Void> {

    private static final Logger log = LoggerFactory.getLogger(CronJobHandler.class);

    @Override
    public Void execute(ScheduledEvent input) {
        log.trace("{}", input);
        return null;
    }
}
