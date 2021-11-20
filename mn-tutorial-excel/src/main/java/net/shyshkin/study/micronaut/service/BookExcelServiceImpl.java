package net.shyshkin.study.micronaut.service;

import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.server.types.files.SystemFile;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.excel.Book;
import net.shyshkin.study.micronaut.excel.BookExcelStylesheet;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class BookExcelServiceImpl implements BookExcelService {

    @NonNull
    @Override
    public SystemFile excelFileFromBooks(@NonNull @NotNull List<@Valid Book> bookList) {

        try {
            File file = File.createTempFile(HEADER_EXCEL_FILE_PREFIX, HEADER_EXCEL_FILE_SUFFIX);
            PoiSpreadsheetBuilder.create(file).build(w -> {
                w.apply(BookExcelStylesheet.class);
                w.sheet(SHEET_NAME, s -> {
                    s.row(r -> Stream.of(HEADER_ISBN, HEADER_NAME)
                            .forEach(header -> r.cell(cd -> {
                                        cd.value(header);
                                        cd.style(BookExcelStylesheet.STYLE_HEADER);
                                    })
                            ));
                    bookList.stream()
                            .forEach(book -> s.row(r -> {
                                r.cell(book.getIsbn());
                                r.cell(book.getName());
                            }));
                });
            });
            return new SystemFile(file).attach(HEADER_EXCEL_FILENAME);
        } catch (IOException e) {
            log.error("File not found exception raised when generating excel file");
        }
        throw new HttpStatusException(HttpStatus.SERVICE_UNAVAILABLE, "error generating excel file");
    }
}
