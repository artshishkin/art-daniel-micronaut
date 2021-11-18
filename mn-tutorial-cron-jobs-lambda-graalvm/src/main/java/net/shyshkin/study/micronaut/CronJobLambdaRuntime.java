package net.shyshkin.study.micronaut;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime;
import net.shyshkin.study.micronaut.graalvm.CronJobHandler;

import java.net.MalformedURLException;
import java.util.Map;

public class CronJobLambdaRuntime extends AbstractMicronautLambdaRuntime<ScheduledEvent, Void, Map<String, Object>, Void> {

    public static void main(String[] args) {
        try {
            new CronJobLambdaRuntime().run(args);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nullable
    protected RequestHandler<Map<String, Object>, Void> createRequestHandler(String... args) {
        return new CronJobHandler();
    }
}
