package net.shyshkin.study.micronaut.service;

import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.server.types.files.SystemFile;
import net.shyshkin.study.micronaut.excel.Book;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@DefaultImplementation(value = BookExcelServiceImpl.class)
public interface BookExcelService {

    String SHEET_NAME = "Books";
    String HEADER_ISBN = "Isbn";
    String HEADER_NAME = "Name";
    String HEADER_EXCEL_FILE_SUFFIX = ".xlsx";
    String HEADER_EXCEL_FILE_PREFIX = "books";
    String HEADER_EXCEL_FILENAME = HEADER_EXCEL_FILE_PREFIX + HEADER_EXCEL_FILE_SUFFIX;

    @NonNull
    SystemFile excelFileFromBooks(@NonNull @NotNull List<@Valid Book> bookList);

}
