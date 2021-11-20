package net.shyshkin.study.micronaut.excel;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Introspected
public class Book {

    @NonNull
    @NotBlank
    private final String isbn;

    @NonNull
    @NotBlank
    private final String name;


}
