package net.shyshkin.study.micronaut.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.types.files.SystemFile;
import io.micronaut.views.View;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.excel.BookRepository;
import net.shyshkin.study.micronaut.service.BookExcelService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BookRepository bookRepository;
    private final BookExcelService bookExcelService;

    @View("index")
    @Get
    public Map<String, String> index() {
        return new HashMap<>();
    }

    @Produces(value = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Get("/excel")
    public SystemFile excel() {
        return bookExcelService.excelFileFromBooks(bookRepository.findAll());
    }
}
