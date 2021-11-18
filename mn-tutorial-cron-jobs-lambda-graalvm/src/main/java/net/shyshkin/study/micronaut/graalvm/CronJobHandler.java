package net.shyshkin.study.micronaut.graalvm;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Introspected
public class CronJobHandler extends MicronautRequestHandler<Map<String, Object>, Void> {

    private static final Logger log = LoggerFactory.getLogger(CronJobHandler.class);

    @Override
    public Void execute(Map<String, Object> input) {
        log.trace("{}", input);
        return null;
    }
}
