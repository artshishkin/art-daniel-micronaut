package net.shyshkin.study.micronaut.thumbnail.graalvm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import net.shyshkin.study.micronaut.thumbnail.graalvm.generator.ThumbnailGenerator;
import net.shyshkin.study.micronaut.thumbnail.graalvm.s3art.S3EventNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.LinkedHashMap;
import java.util.List;

@Introspected
public class ThumbnailHandler extends MicronautRequestHandler<Object, Void> {

    private static final Logger log = LoggerFactory.getLogger(ThumbnailHandler.class);

    public static final String OBJECT_CREATED = "ObjectCreated";

    private static final String SLASH = "/";
    private static final String THUMBNAILS = "thumbnails";

    private static final List<String> ALLOW_FORMAT = List.of("jpg", "png");

    private static final char DOT = '.';

    @Inject
    public ThumbnailGenerator thumbnailGenerator;

    @Inject
    public S3Client s3Client;

    @Inject
    public ObjectMapper objectMapper;

    @Override
    public Void execute(Object input) {
        log.debug("input: {}", input);
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) input;
        log.debug("LinkedHashMap: {}", map);
        S3EventNotification s3Event = null;
        String jsonInput = null;
        try {
            jsonInput = objectMapper.writeValueAsString(map);
            log.debug("JSON: {}", jsonInput);
            s3Event = objectMapper.readValue(jsonInput, S3EventNotification.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.debug("s3Event: {}", s3Event);

        Object records = map.get("Records");

        log.debug("Record class: {}", ((List<Object>)records).get(0).getClass());
        log.debug("Record: {}", ((List<Object>)records).get(0));



//        input.getRecords()
//                .stream()
//                .peek(record -> log.info("event name: {}", record.getEventName()))
//                .filter(record -> record.getEventName().contains(OBJECT_CREATED))
//                .map(S3EventNotification.S3EventNotificationRecord::getS3)
//                .forEach(this::persistThumbnail);
        return null;
    }

//    private void persistThumbnail(S3EventNotification.S3Entity s3Entity) {
//        String bucket = s3Entity.getBucket().getName();
//        String key = s3Entity.getObject().getKey();
//
//        getFileFormat(key)
//                .filter(ALLOW_FORMAT::contains)
//                .flatMap(format -> thumbnailGenerator.thumbnail(
//                        s3Client.getObject(
//                                GetObjectRequest.builder()
//                                        .bucket(bucket)
//                                        .key(key)
//                                        .build()),
//                        format)
//                )
//                .map(bytes -> {
//                    log.debug("Image size: {}", bytes.length);
//                    return bytes;
//                })
//                .map(bytes -> s3Client.putObject(
//                        PutObjectRequest.builder()
//                                .key(thumbnailKey(key))
//                                .bucket(bucket)
//                                .build(),
//                        RequestBody.fromBytes(bytes))
//                )
//                .ifPresent(response -> log.info("ThumbnailSaved: {}", response));
//    }
//
//    private Optional<String> getFileFormat(String key) {
//        int index = key.lastIndexOf(DOT);
//        return (index != -1) ?
//                Optional.of(key.substring(index + 1).toLowerCase(Locale.ENGLISH)) :
//                Optional.empty();
//    }
//
//    private static String thumbnailKey(String key) {
//        int index = key.lastIndexOf(SLASH);
//        String fileName = index != -1 ? key.substring(index + SLASH.length()) : key;
//        return THUMBNAILS + SLASH + fileName;
//    }
}
