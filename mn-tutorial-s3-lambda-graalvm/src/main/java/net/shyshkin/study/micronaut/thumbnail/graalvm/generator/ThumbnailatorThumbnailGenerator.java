package net.shyshkin.study.micronaut.thumbnail.graalvm.generator;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import net.coobird.thumbnailator.Thumbnails;
import net.shyshkin.study.micronaut.thumbnail.graalvm.ThumbnailConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Singleton
public class ThumbnailatorThumbnailGenerator implements ThumbnailGenerator {

    private static final Logger log = LoggerFactory.getLogger(ThumbnailatorThumbnailGenerator.class);
    private final ThumbnailConfiguration thumbnailConfig;

    public ThumbnailatorThumbnailGenerator(ThumbnailConfiguration thumbnailConfiguration) {
        this.thumbnailConfig = thumbnailConfiguration;
    }

    @NonNull
    @Override
    public Optional<byte[]> thumbnail(@NonNull InputStream inputStream, @NonNull @NotBlank @Pattern(regexp = "jpg|png") String format) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Thumbnails.of(inputStream)
                    .size(thumbnailConfig.getWidth(), thumbnailConfig.getHeight())
                    .toOutputStream(outputStream);
            return Optional.of(outputStream.toByteArray());
        } catch (IOException exception) {
            log.warn("IOException thrown while generating the thumbnail");
        }
        return Optional.empty();
    }
}
