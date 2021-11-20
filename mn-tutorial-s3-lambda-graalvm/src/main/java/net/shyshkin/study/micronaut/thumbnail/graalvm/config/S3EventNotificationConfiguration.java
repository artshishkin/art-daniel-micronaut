package net.shyshkin.study.micronaut.thumbnail.graalvm.config;

import com.amazonaws.services.s3.event.S3EventNotification;
import io.micronaut.core.annotation.Introspected;

@Introspected(classes = {
        S3EventNotification.class,
        S3EventNotification.S3EventNotificationRecord.class,
        S3EventNotification.RequestParametersEntity.class,
        S3EventNotification.ResponseElementsEntity.class,
        S3EventNotification.RestoreEventDataEntity.class,
        S3EventNotification.S3Entity.class,
        S3EventNotification.S3ObjectEntity.class,
        S3EventNotification.S3BucketEntity.class,
        S3EventNotification.UserIdentityEntity.class,
        S3EventNotification.GlacierEventDataEntity.class,
})
public class S3EventNotificationConfiguration {
}
