package net.shyshkin.study.micronaut.excel;

import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;

import java.util.List;

@DefaultImplementation(BookRepositoryImpl.class)
public interface BookRepository {

    @NonNull
    List<Book> findAll();

}
