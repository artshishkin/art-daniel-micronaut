package net.shyshkin.study.micronaut;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime;
import net.shyshkin.study.micronaut.thumbnail.graalvm.ThumbnailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThumbnailLambdaRuntime extends AbstractMicronautLambdaRuntime<S3EventNotification, Void, S3EventNotification, Void> {

    public static final Logger log = LoggerFactory.getLogger(ThumbnailLambdaRuntime.class);

    public static void main(String[] args) {
        try {
            new ThumbnailLambdaRuntime().run(args);

        } catch (Exception e) {
            log.error("Exception in ", e);
        }
    }

    @Override
    @Nullable
    protected RequestHandler<S3EventNotification, Void> createRequestHandler(String... args) {
        return new ThumbnailHandler();
    }
}
