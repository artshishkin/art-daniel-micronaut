package net.shyshkin.study.micronaut.thumbnail.graalvm;

import io.micronaut.core.annotation.NonNull;

public interface ThumbnailConfiguration {

    @NonNull
    Integer getWidth();

    @NonNull
    Integer getHeight();

}
