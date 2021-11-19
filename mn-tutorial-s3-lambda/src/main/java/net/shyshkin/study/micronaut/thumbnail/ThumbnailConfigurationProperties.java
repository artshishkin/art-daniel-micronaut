package net.shyshkin.study.micronaut.thumbnail;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(value = "app.thumbnail")
public class  ThumbnailConfigurationProperties implements ThumbnailConfiguration {

    @NonNull
    @NotNull
    private Integer width;

    @NonNull
    @NotNull
    private Integer height;

    @Override
    @NonNull
    public Integer getWidth() {
        return width;
    }

    public void setWidth(@NonNull Integer width) {
        this.width = width;
    }

    @Override
    @NonNull
    public Integer getHeight() {
        return height;
    }

    public void setHeight(@NonNull Integer height) {
        this.height = height;
    }
}
